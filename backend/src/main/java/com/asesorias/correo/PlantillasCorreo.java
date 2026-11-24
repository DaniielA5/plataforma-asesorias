package com.asesorias.correo;

import org.springframework.web.util.HtmlUtils;

/**
 * Arma el HTML y el texto plano de los correos de la app.
 * Los estilos van en línea (style="...") porque Gmail y Outlook ignoran las hojas de estilo,
 * y el diseño es de tablas porque es lo único que todos los clientes de correo dibujan igual.
 */
public final class PlantillasCorreo {

    /** Un correo listo para enviar: el HTML y su versión en texto plano. */
    public record Contenido(String html, String texto) {
    }

    private static final String MARCO = """
            <!DOCTYPE html>
            <html lang="es">
            <head>
              <meta charset="UTF-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0">
              <meta name="color-scheme" content="light">
              <title>AyudITO</title>
            </head>
            <body style="margin:0;padding:0;background-color:#F2EDE4;">
              <div style="display:none;max-height:0;overflow:hidden;opacity:0;color:#F2EDE4;">{{resumen}}</div>
              <table role="presentation" width="100%" cellpadding="0" cellspacing="0" style="background-color:#F2EDE4;">
                <tr>
                  <td align="center" style="padding:32px 16px;">
                    <table role="presentation" width="560" cellpadding="0" cellspacing="0" style="width:100%;max-width:560px;">
                      <tr>
                        <td align="center" style="padding-bottom:20px;">
                          <table role="presentation" cellpadding="0" cellspacing="0">
                            <tr>
                              <td align="center" width="40" height="40" style="width:40px;height:40px;background-color:#263128;border-radius:12px;font-family:Georgia,'Times New Roman',serif;font-size:22px;font-weight:bold;color:#F2EDE4;">A</td>
                              <td style="padding-left:10px;font-family:Arial,Helvetica,sans-serif;font-size:22px;font-weight:bold;color:#263128;">Ayud<span style="color:#A8523A;">ITO</span></td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <tr>
                        <td style="background-color:#FBF7EF;border:1px solid #DED5C2;border-radius:18px;padding:36px 32px;font-family:Arial,Helvetica,sans-serif;font-size:16px;line-height:1.6;color:#1F2A22;">
                          {{contenido}}
                        </td>
                      </tr>
                      <tr>
                        <td align="center" style="padding:20px 8px 0;font-family:Arial,Helvetica,sans-serif;font-size:12px;line-height:1.5;color:#7A7F76;">
                          {{pie}}
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </body>
            </html>
            """;

    private PlantillasCorreo() {
    }

    public static Contenido bienvenida(String nombre, String correo, String urlFrontend) {
        String primerNombre = esc(nombre.trim().split("\\s+")[0]);
        String enlace = esc(urlFrontend + "/asesorias");

        String contenido = titulo("¡Bienvenido a AyudITO, " + primerNombre + "!")
                + parrafo("Tu cuenta ya está lista. Aquí puedes encontrar asesorías con profesores y apartar tu lugar en un par de clics.")
                + "<p style=\"margin:24px 0 4px;font-weight:bold;color:#263128;\">Con tu cuenta puedes:</p>"
                + "<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">"
                + punto("", "Explorar el catálogo", "Busca asesorías por materia, fecha y lugar.")
                + punto("", "Inscribirte con un clic", "Aparta tu lugar mientras haya cupo.")
                + punto("", "Recibir avisos", "Agrega tu teléfono en Mi perfil y te avisamos por SMS o WhatsApp si una asesoría se cancela.")
                + "</table>"
                + boton("Explorar asesorías", enlace);

        String pie = "Recibiste este correo porque se creó una cuenta en AyudITO con " + esc(correo) + ".<br>"
                + "Si no fuiste tú, puedes ignorarlo.";

        String html = MARCO
                .replace("{{resumen}}", "Tu cuenta ya está lista. Estas son las primeras cosas que puedes hacer.")
                .replace("{{pie}}", pie)
                .replace("{{contenido}}", contenido);

        String texto = "¡Bienvenido a AyudITO, " + nombre.trim().split("\\s+")[0] + "!\n\n"
                + "Tu cuenta ya está lista. Aquí puedes encontrar asesorías con profesores y apartar tu lugar en un par de clics.\n\n"
                + "Con tu cuenta puedes:\n"
                + "- Explorar el catálogo: busca asesorías por materia, fecha y lugar.\n"
                + "- Inscribirte con un clic: aparta tu lugar mientras haya cupo.\n"
                + "- Recibir avisos: agrega tu teléfono en Mi perfil y te avisamos por SMS o WhatsApp si una asesoría se cancela.\n\n"
                + "Explorar asesorías: " + urlFrontend + "/asesorias\n\n"
                + "Recibiste este correo porque se creó una cuenta en AyudITO con " + correo + ". Si no fuiste tú, puedes ignorarlo.\n";

        return new Contenido(html, texto);
    }

    public static Contenido recuperacion(String enlaceCrudo) {
        String enlace = esc(enlaceCrudo);

        String contenido = titulo("Restablece tu contraseña")
                + parrafo("Recibimos una solicitud para crear una contraseña nueva en tu cuenta de AyudITO. Pulsa el botón para elegirla.")
                + boton("Crear contraseña nueva", enlace)
                + "<p style=\"margin:0 0 20px;font-size:14px;color:#5C6A5F;\">El enlace vence en <strong>30 minutos</strong> y solo se puede usar una vez.</p>"
                + "<p style=\"margin:0;padding-top:16px;border-top:1px solid #DED5C2;font-size:13px;line-height:1.5;color:#5C6A5F;\">"
                + "¿El botón no funciona? Copia y pega este enlace en tu navegador:<br>"
                + "<a href=\"" + enlace + "\" style=\"color:#A8523A;word-break:break-all;\">" + enlace + "</a></p>";

        String pie = "Si tú no pediste este cambio, ignora este correo: tu contraseña seguirá igual.";

        String html = MARCO
                .replace("{{resumen}}", "Crea una contraseña nueva. El enlace vence en 30 minutos.")
                .replace("{{pie}}", pie)
                .replace("{{contenido}}", contenido);

        String texto = "Restablece tu contraseña\n\n"
                + "Recibimos una solicitud para crear una contraseña nueva en tu cuenta de AyudITO.\n"
                + "Abre este enlace para elegirla (vence en 30 minutos y solo se puede usar una vez):\n\n"
                + enlaceCrudo + "\n\n"
                + "Si tú no pediste este cambio, ignora este correo: tu contraseña seguirá igual.\n";

        return new Contenido(html, texto);
    }

    public static Contenido aviso(String encabezado, String mensaje) {
        String contenido = titulo(esc(encabezado)) + parrafo(esc(mensaje));
        String pie = "Recibiste este aviso porque tienes una cuenta en AyudITO.";

        String html = MARCO
                .replace("{{resumen}}", esc(mensaje))
                .replace("{{pie}}", pie)
                .replace("{{contenido}}", contenido);

        String texto = encabezado + "\n\n" + mensaje + "\n\n" + pie + "\n";

        return new Contenido(html, texto);
    }

    // ---- piezas reutilizables ----

    private static String titulo(String texto) {
        return "<h1 style=\"margin:0 0 16px;font-family:Georgia,'Times New Roman',serif;font-size:26px;line-height:1.25;color:#263128;\">"
                + texto + "</h1>";
    }

    private static String parrafo(String texto) {
        return "<p style=\"margin:0 0 8px;\">" + texto + "</p>";
    }

    private static String punto(String icono, String titulo, String texto) {
        return "<tr>"
                + "<td valign=\"top\" width=\"40\" style=\"width:40px;padding:8px 0;font-size:22px;line-height:1.2;\">" + icono + "</td>"
                + "<td style=\"padding:8px 0;font-size:15px;line-height:1.5;\">"
                + "<strong style=\"color:#263128;\">" + titulo + "</strong><br>"
                + "<span style=\"color:#5C6A5F;\">" + texto + "</span></td>"
                + "</tr>";
    }

    // El fondo es un terracota un poco más oscuro que el de la app para que el texto claro se lea bien.
    private static String boton(String texto, String urlYaEscapada) {
        return "<table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin:28px 0;\"><tr>"
                + "<td style=\"background-color:#A8523A;border-radius:10px;\">"
                + "<a href=\"" + urlYaEscapada + "\" style=\"display:inline-block;padding:14px 28px;font-family:Arial,Helvetica,sans-serif;font-size:16px;font-weight:bold;color:#FBF7EF;text-decoration:none;\">"
                + texto + "</a></td></tr></table>";
    }

    private static String esc(String valor) {
        return HtmlUtils.htmlEscape(valor);
    }
}