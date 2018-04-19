package com.nahtredn.utilities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.nahtredn.entities.CurrentStudy;
import com.nahtredn.entities.Documentation;
import com.nahtredn.entities.General;
import com.nahtredn.entities.StudyDone;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class PDF {

    private static final String RUTA_DOCUMENTO = Environment.getRootDirectory() + "/j2w/solicitudes";

    private Font times_font = new Font(FontFactory.getFont(BaseFont.TIMES_ROMAN, 10));
    private BaseFont times_campo = times_font.getCalculatedBaseFont(false);
    private Font times_t = new Font(FontFactory.getFont(BaseFont.TIMES_ROMAN, 12));
    private BaseFont time_texto = times_t.getCalculatedBaseFont(false);
    private Font times_bold = new Font(FontFactory.getFont(BaseFont.TIMES_ROMAN, 12, Font.BOLD));
    private BaseFont base_time_bold = times_bold.getCalculatedBaseFont(false);
    private Font times_section = new Font(FontFactory.getFont(BaseFont.TIMES_ROMAN, 11, Font.BOLD));
    private BaseFont section = times_section.getCalculatedBaseFont(false);


    // Instancia de la clase
    private static PDF instance;
    // Contexto en el que se mostrará el Toast con el mensaje
    private static Context context;

    /**
     * Método constructor de la clase
     *
     * @param contexto corresponde al contexto en el cual se mostrarán los mensajes.
     */
    public PDF(Context contexto) {
        context = contexto;
    }

    /**
     * Método que devuelve una instancia del Mensajero, si aún no existe, la crea.
     *
     * @param application corresponde a la aplicación que lo manda ejecutar
     * @return una instancia del Mensajero
     */
    public static PDF with(Application application) {
        if (instance == null) {
            instance = new PDF(application.getApplicationContext());
            context = application.getApplicationContext();
        }
        return instance;
    }

    /**
     * Método que devuelve una instancia del Mensajero, si aún no existe, la crea.
     *
     * @param activity corresponde al activity que lo manda ejecuutar.
     * @return una instancia del Mensajero
     */
    public static PDF with(Activity activity) {
        if (instance == null) {
            instance = new PDF(activity.getApplication().getApplicationContext());
            context = activity.getApplicationContext();
        }
        return instance;
    }

    private void loadImage(Document document) {
        String pathPhoto = getImageUrl();
        if (pathPhoto != null) {
            File imgFile = new File(pathPhoto);
            if (imgFile.exists()) {
                Image img = null;
                try {
                    img = Image.getInstance(imgFile.getAbsolutePath());
                    img.setAlignment(Element.ALIGN_CENTER);
                    img.scaleToFit(75f, 75f);
                    img.setAbsolutePosition(485f, 753f);
                    document.add(img);
                } catch (DocumentException | IOException de) {
                    Log.w("PDF", "Error: " + de.getMessage());
                }
            }
        }
    }

    private String getImageUrl() {
        SharedPreferences prefs = context.getSharedPreferences("photoPreferences", Context.MODE_PRIVATE);
        return prefs.getString("photo", null);
    }

    private void drawLine(PdfContentByte canvas, int startX, int startY, int endX, int endY) {
        canvas.saveState();
        canvas.setLineWidth(1f);
        canvas.setRGBColorStroke(96, 125, 139);
        canvas.moveTo(startX, startY);
        canvas.lineTo(endX, endY);
        canvas.stroke();
        canvas.restoreState();
    }

    private void drawLines(PdfContentByte canvas, int[] startX, int[] startY, int[] endX, int[] endY) {
        for (int i = 0; i < startX.length; i++) {
            canvas.saveState();
            canvas.setLineWidth(1f);
            canvas.setRGBColorStroke(96, 125, 139);
            canvas.moveTo(startX[i], startY[i]);
            canvas.lineTo(endX[i], endY[i]);
            canvas.stroke();
            canvas.restoreState();
        }
    }

    private void drawLinesForStudiesDone(PdfContentByte canvas, int startBorder, int endBorder, int[] columns, int numStudiesDone){
        drawLine(canvas,23,startBorder,560,startBorder);
        drawLine(canvas,23,endBorder,560,endBorder);
        drawLine(canvas,23,startBorder,23,endBorder);
        drawLine(canvas,560,startBorder,560,endBorder);
        for (int i : columns){
            drawLine(canvas,i,startBorder,i,endBorder);
        }
        for (int i = 0; i < numStudiesDone; i++){
            startBorder = startBorder - 25;
            drawLine(canvas,23,startBorder,560,startBorder);
            startBorder = startBorder - 25;
            drawLine(canvas,23,startBorder,560,startBorder);
        }
    }

    private void drawLabel(PdfContentByte canvas, String text, int x, int y) {
        canvas.setRGBColorFill(213, 0, 0);
        canvas.beginText();
        canvas.setFontAndSize(times_campo, 8);
        canvas.showTextAligned(0, text, x, y, 0);
        canvas.endText();
    }

    private void drawTitle(PdfContentByte canvas, String text, int x, int y) {
        canvas.setRGBColorFill(0, 0, 0);
        canvas.beginText();
        canvas.setFontAndSize(base_time_bold, 15);
        canvas.showTextAligned(0, text, x, y, 0);
        canvas.endText();
    }

    private void drawText(PdfContentByte canvas, String text, int x, int y) {
        canvas.setRGBColorFill(0, 0, 0);
        canvas.beginText();
        canvas.setFontAndSize(time_texto, 12);
        canvas.showTextAligned(0, text != null ? text : "", x, y, 0);
        canvas.endText();
    }

    public boolean generaSolicitud(){
        General general = RealmController.with(context).find(new General());
        String nombreArchivo = general.getName().toLowerCase().replace(" ", "_") + ".pdf";
        Document pdf = new Document();
        File f = crearFichero(nombreArchivo);
        if (f == null){
            return false;
        }
        // File f = new File(context.getFilesDir(), nombreArchivo);
        Rectangle rTemp = pdf.getPageSize();
        Rectangle rPageTemp = new Rectangle(rTemp.getWidth(), rTemp.getHeight());
        String color = RealmController.with(context).find(PreferencesProperties.BACKGROUND_DOCUMENT.toString());
        if (color.equalsIgnoreCase("Beige")) {
            rPageTemp.setBackgroundColor(new harmony.java.awt.Color(252, 243, 207));
        }
        pdf.setPageSize(rPageTemp);
        FileOutputStream ficheroPdf = null;
        try {
            ficheroPdf = new FileOutputStream(f.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(pdf, ficheroPdf);
        } catch (DocumentException e) {
            Log.w("Error: ", e.getCause());
            e.printStackTrace();
            return false;
        }
        pdf.open();
        PdfContentByte canvas = writer.getDirectContent();
        drawFooter(canvas);
        loadImage(pdf);
        drawLines(canvas, new int[]{484, 484, 484, 561}, new int[]{829, 752, 752, 752}, new int[]{561, 484, 561, 561}, new int[]{829, 829, 752, 829});
        drawTitle(canvas, "SOLICITUD DE EMPLEO DIGITAL", 30, 810);
        drawLabel(canvas, "Fecha:", 30, 790);
        drawText(canvas, android.text.format.DateFormat.getDateFormat(context).format(Calendar.getInstance().getTime()), 30, 780);
        drawLines(canvas, new int[]{23, 23, 23, 170}, new int[]{800, 775, 800, 800}, new int[]{170, 170, 23, 170}, new int[]{800, 775, 775, 775});
        drawTitle(canvas, "DATOS PERSONALES", 30, 750);
        drawLabel(canvas, "Nombre(s):", 30, 730);
        drawText(canvas, general.getName(), 30, 720);
        drawLabel(canvas, "Apellido Paterno:", 160, 730);
        drawText(canvas, general.getLastName(), 160, 720);
        drawLines(canvas, new int[]{155, 235, 325, 405}, new int[]{740, 740, 740, 740}, new int[]{155, 235, 325, 405}, new int[]{715, 715, 715, 715});
        drawLabel(canvas, "Apellido Materno:", 240, 730);
        drawText(canvas, general.getMiddleName(), 240, 720);
        drawLabel(canvas, "Fecha de Nacimiento:", 330, 730);
        drawText(canvas, android.text.format.DateFormat.getDateFormat(context).format(general.getBirthDate()), 330, 720);
        drawLabel(canvas, "Edad:", 410, 730);
        drawText(canvas, general.getAge() + "", 410, 720);
        drawLabel(canvas, "Estado Civil:", 440, 730);
        drawText(canvas, general.getCivilStatus(), 440, 720);
        drawLines(canvas, new int[]{435, 505, 23, 23, 23, 560}, new int[]{740,740,740,715,740,740}, new int[]{435,505,560,560,23,560}, new int[]{715,715,740,715,665,690});
        drawLabel(canvas, "Vive con: ", 510, 730);
        drawText(canvas, general.getLivingWith(), 510, 720);
        drawLabel(canvas, "Calle y número:", 30, 705);
        drawText(canvas, general.getAddress().getStreet(), 30, 695);
        drawLabel(canvas, "Colonia:", 210, 705);
        drawText(canvas, general.getAddress().getColony(), 210, 695);
        drawLabel(canvas, "Código Postal:", 340, 705);
        drawText(canvas, general.getAddress().getZipCode(), 340, 695);
        drawLabel(canvas, "Municipio o Delegación:", 400, 705);
        drawText(canvas, general.getAddress().getMunicipality(), 400, 695);
        drawLabel(canvas, "Entidad federativa:", 30, 680);
        drawText(canvas, general.getAddress().getState(), 30, 670);
        drawLabel(canvas, "Género:", 210, 680);
        drawText(canvas, general.getGenre(), 210, 670);
        drawLines(canvas, new int[]{205,270,23,95,395,23,335,205}, new int[]{690,690,665,635,715,690,715,715},
                new int[]{205,270,270,95,395,560,335,205}, new int[]{665,665,665,610,690,690,690,690});
        drawTitle(canvas, "DOCUMENTACIÓN", 30, 645);
        drawLabel(canvas, "Teléfono:", 30, 625);
        drawText(canvas, general.getPhone(), 30, 615);
        drawLabel(canvas, "Correo electrónico:", 100, 625);
        drawText(canvas, general.getEmail(), 100, 615);
        drawLabel(canvas, "Licencia de Manejo:", 330, 625);
        Documentation documentation = RealmController.with(context).find(new Documentation());
        drawText(canvas, documentation.getDriverLicense(), 330, 615);
        drawLabel(canvas, "CURP:", 410, 625);
        drawText(canvas, documentation.getCurp(), 410, 615);
        drawLabel(canvas, "RFC:", 30, 600);
        drawText(canvas, documentation.getRfc(), 30, 590);
        drawLines(canvas, new int[]{325,405,23,23,23,23,560,140}, new int[]{635,635,610,635,585,635,635,610},
                new int[]{325,405,560,560,140,23,560,140}, new int[]{610,610,610,635,585,585,610,585});

        // Draw aditional data

        int bordeTemp = 565;
        drawTitle(canvas, "ESTUDIOS REALIZADOS", 30, bordeTemp);
        bordeTemp = bordeTemp - 10;
        int startBorder = bordeTemp - 5, endBorder;
        List<StudyDone> studyDones = RealmController.with(context).findAllStudiesDone();
        for (StudyDone studyDone : studyDones){
            bordeTemp = bordeTemp - 15;
            drawLabel(canvas, "Nivel académico:", 30, bordeTemp);
            drawLabel(canvas, "Nombre del curso:", 120, bordeTemp);
            drawLabel(canvas, "Escuela o institución:", 320, bordeTemp);

            bordeTemp = bordeTemp - 10;

            drawText(canvas, studyDone.getAcademicLevel(), 30, bordeTemp);
            drawText(canvas, studyDone.getCourseName(), 120, bordeTemp);
            drawText(canvas, studyDone.getInstitute(), 320, bordeTemp);

            bordeTemp = bordeTemp - 15;

            drawLabel(canvas, "Título obtenido:", 30, bordeTemp);
            drawLabel(canvas, "Fecha:", 120, bordeTemp);
            drawLabel(canvas, "Estado:", 320, bordeTemp);

            bordeTemp = bordeTemp - 10;

            drawText(canvas, studyDone.getTitle(), 30, bordeTemp);
            drawText(canvas, studyDone.getStartDate() + " - " + studyDone.getEndDate(), 120, bordeTemp);
            drawText(canvas, studyDone.getState(), 320, bordeTemp);
        }
        endBorder = bordeTemp - 5;

        drawLinesForStudiesDone(canvas, startBorder, endBorder, new int[]{115,315},studyDones.size());

        bordeTemp -= 30;

        drawTitle(canvas, "ESTUDIOS ACTUALES", 30, bordeTemp);
        bordeTemp = bordeTemp - 10;
        startBorder = bordeTemp -5;
        List<CurrentStudy> currentStudies = RealmController.with(context).findAllCurrentStudies();

        if (!currentStudies.isEmpty()){
            for (CurrentStudy currentStudy : currentStudies){
                bordeTemp = bordeTemp - 15;
                drawLabel(canvas, "Nivel académico:", 30, bordeTemp);
                drawLabel(canvas, "Nombre del curso:", 120, bordeTemp);
                drawLabel(canvas, "Escuela o institución:", 320, bordeTemp);

                bordeTemp = bordeTemp - 10;

                drawText(canvas, currentStudy.getAcademicLevel(), 30, bordeTemp);
                drawText(canvas, currentStudy.getCourseName(), 120, bordeTemp);
                drawText(canvas, currentStudy.getInstitute(), 320, bordeTemp);
                drawLine(canvas,315,startBorder,315,bordeTemp - 5);

                bordeTemp = bordeTemp - 15;

                drawLabel(canvas, "Grado:", 30, bordeTemp);
                drawLabel(canvas, "Horario:", 120, bordeTemp);

                bordeTemp = bordeTemp - 10;

                drawText(canvas, currentStudy.getFullDegree(), 30, bordeTemp);
                drawText(canvas, currentStudy.getFullSchedule(), 120, bordeTemp);
            }

            endBorder = bordeTemp - 5;

            drawLinesForStudiesDone(canvas, startBorder, endBorder, new int[]{115},currentStudies.size());
        }

        pdf.close();
        return true;
    }

    private boolean canWriteOnExternalStorage(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    private void drawFooter(PdfContentByte canvas) {
        canvas.setRGBColorFill(0, 0, 0);
        canvas.beginText();
        canvas.setFontAndSize(time_texto, 5);
        canvas.showTextAligned(0, "solicitud de empleo dígital generada por J2W"
                + ", el uso o distribución de la misma son responsabilidad del"
                + " usuario que la genero deslindando a J2W de cualquier incidente"
                + " y/o daño que se pueda generar al usuario o a terceros.", 50, 10, 0);
        canvas.endText();

        canvas.setRGBColorFill(176, 190, 197);
        canvas.beginText();
        canvas.setFontAndSize(time_texto, 10);
        canvas.showTextAligned(0, "ADSOpdf versión 4.0", 505, 5, 0);
        canvas.endText();
    }

    private File crearFichero(String nombreArchivo) {
        File f = null;
        if(canWriteOnExternalStorage()){
            f = new File(Environment.getExternalStorageDirectory(), nombreArchivo);
            RealmController.with(context).save(PreferencesProperties.PATH_FILE.toString(), f.getPath());
        }
        return f;
    }
}
