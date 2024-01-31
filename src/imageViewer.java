import Interface.Panels.PanelPrincipal;

class imageViewer {
    public static void main(String[] args) {
        String imagePath = ".\\docs\\astronaut-guardian.jpg";
        // String imagePath = "D:/Default/Proyectos/Cursos/Curso-java/Tutorials/Joogle/docs/expected_output_lm.png";
        // TODO: implement JFileChooser selection of image for PanelPrincipal
        new PanelPrincipal(
                1920,
                1080,
                imagePath
        );
    }
}
