package com.cyberpath.springboot.servicio.impl.ejercicio;

import com.cyberpath.springboot.dto.ejercicio.OpcionDto;
import com.cyberpath.springboot.modelo.ejercicio.Opcion;
import com.cyberpath.springboot.modelo.ejercicio.Pregunta;
import lombok.AllArgsConstructor;
import com.cyberpath.springboot.modelo.ejercicio.Ejercicio;
import com.cyberpath.springboot.repositorio.ejercicio.EjercicioRepositorio;
import com.cyberpath.springboot.servicio.ejercicio.EjercicioServicio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class EjercicioImpl implements EjercicioServicio {
    private final EjercicioRepositorio ejercicioRepositorio;

    @Override
    public List<Ejercicio> getAll() {
        return ejercicioRepositorio.findAll();
    }

    @Override
    public Ejercicio getById(Integer id) {
        return ejercicioRepositorio.findById(id).orElse(null);
    }

    @Override
    public Ejercicio save(Ejercicio ejercicio) {
        return ejercicioRepositorio.save(ejercicio);
    }

    @Override
    public void delete(Integer id) {
        ejercicioRepositorio.deleteById(id);
    }

    @Override
    public Ejercicio update(Integer id, Ejercicio ejercicio) {
        Ejercicio aux = ejercicioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado"));
        aux.setNombre(ejercicio.getNombre());
        aux.setHecho(ejercicio.getHecho());
        aux.setSubtema(ejercicio.getSubtema());

        return ejercicioRepositorio.save(aux);
    }

    @Override
    public void sincronizarOpciones(Pregunta pregunta, List<OpcionDto> opcionesDto) {

        // Opciones actuales en BD
        List<Opcion> opcionesActuales = pregunta.getOpciones();

        // IDs que vienen del frontend
        Set<Integer> idsDto = opcionesDto.stream()
                .map(OpcionDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Eliminar opciones que ya no existen en el DTO
        opcionesActuales.removeIf(opcion ->
                opcion.getId() != null && !idsDto.contains(opcion.getId())
        );

        // 4. Procesar DTOs (update o create)
        for (OpcionDto dto : opcionesDto) {

            // UPDATE
            if (dto.getId() != null) {
                Opcion opcionExistente = opcionesActuales.stream()
                        .filter(o -> o.getId().equals(dto.getId()))
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Opción no encontrada: " + dto.getId()
                                )
                        );

                opcionExistente.setTexto(dto.getTexto());
                opcionExistente.setCorrecta(dto.isCorrecta());

            }
            // CREATE
            else {
                Opcion nueva = Opcion.builder()
                        .texto(dto.getTexto())
                        .correcta(dto.isCorrecta())
                        .pregunta(pregunta)
                        .build();

                opcionesActuales.add(nueva);
            }
        }
    }
}