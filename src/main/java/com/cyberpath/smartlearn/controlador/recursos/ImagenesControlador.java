package com.cyberpath.smartlearn.controlador.recursos;

import com.cyberpath.smartlearn.modelo.contenido.Materia;
import com.cyberpath.smartlearn.servicio.implementacion.recurso.ImagenesImpl;
import com.cyberpath.smartlearn.servicio.servicio.contenido.MateriaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/smartlearn/api/imagenes")
public class ImagenesControlador {

    @Autowired
    private ImagenesImpl cloudinaryService;

    @Autowired
    private MateriaServicio materiaServicio;

    @PostMapping("/subir")
    public Materia subirImagen(
            @RequestParam("file") MultipartFile file,
            @RequestParam("idMateria") Integer idMateria)
            throws IOException {

        String url = cloudinaryService.uploadFile(file);

        Materia materia = materiaServicio.findById(idMateria);

        if (materia == null) {
            throw new RuntimeException("Materia no encontrada");
        }

        materia.setSlug(url);

        return materiaServicio.update(materia.getId(), materia);
    }
}