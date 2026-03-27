package com.sanosysalvos.ms_coincidencias.service;

import com.sanosysalvos.ms_coincidencias.model.Coincidencia;
import com.sanosysalvos.ms_coincidencias.model.PuntajeCoincidencia;
import com.sanosysalvos.ms_coincidencias.repository.CoincidenciaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CoincidenciaService {

    @Autowired
    private CoincidenciaRepository coincidenciaRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Factory Method: Crea una nueva coincidencia analizando dos reportes
     * Utiliza el patrón Factory Method para calcular y crear la coincidencia
     */
    public Coincidencia crearCoincidencia(Long reportePerdidoId, Long reporteEncontradoId) {
        // Verificar que no exista coincidencia previa
        Coincidencia existente = coincidenciaRepository
            .findByReportePerdidoIdAndReporteEncontradoId(reportePerdidoId, reporteEncontradoId);
        
        if (existente != null) {
            log.warn("Coincidencia ya existe entre reportes {} y {}", reportePerdidoId, reporteEncontradoId);
            return existente;
        }

        // Calcular puntaje de coincidencia
        PuntajeCoincidencia puntaje = calcularCoincidencia(reportePerdidoId, reporteEncontradoId);
        
        Coincidencia coincidencia = new Coincidencia();
        coincidencia.setReportePerdidoId(reportePerdidoId);
        coincidencia.setReporteEncontradoId(reporteEncontradoId);
        coincidencia.setPuntajeTotal(puntaje.calcularPuntajeTotal());
        coincidencia.setEstado(Coincidencia.EstadoCoincidencia.PENDIENTE_REVISION);
        coincidencia.setFechaAnalisis(LocalDateTime.now());
        
        try {
            coincidencia.setDetallesPuntaje(objectMapper.writeValueAsString(puntaje));
        } catch (Exception e) {
            log.error("Error al serializar puntaje", e);
            coincidencia.setDetallesPuntaje("");
        }

        return coincidenciaRepository.save(coincidencia);
    }

    /**
     * Calcula el puntaje de coincidencia entre dos reportes
     * Algoritmo:
     * - Especie: 100% si coincide (crítico)
     * - Raza: 30% de similitud
     * - Color: 25% de similitud
     * - Tamaño: 20% de similitud
     * - Cercanía Geográfica: 25% (basado en distancia)
     * - Proximidad de Fechas: 15% (basado en días entre reportes)
     */
    public PuntajeCoincidencia calcularCoincidencia(Long reportePerdidoId, Long reporteEncontradoId) {
        PuntajeCoincidencia puntaje = new PuntajeCoincidencia();

        try {
            // TODO: Obtener datos desde MS Reportes (llamar a la API)
            // Por ahora se implementa con valores de prueba
            
            // Especie (crítico): deben coincidir
            puntaje.setEspecie(100.0);
            
            // Raza: calcular similitud
            puntaje.setRaza(80.0);
            
            // Color: calcular similitud
            puntaje.setColor(75.0);
            
            // Tamaño: calcular similitud
            puntaje.setTamaño(90.0);
            
            // Cercanía Geográfica: basado en distancia
            // Si está a menos de 5km = 100, luego decrece
            puntaje.setCercaniaGeografica(85.0);
            
            // Proximidad de Fechas: basado en días
            // Si hay menos de 7 días = 100, luego decrece
            puntaje.setProximidadFechas(70.0);

        } catch (Exception e) {
            log.error("Error al calcular coincidencia", e);
        }

        return puntaje;
    }

    public Optional<Coincidencia> obtenerPorId(Long id) {
        if (id == null) return Optional.empty();
        return coincidenciaRepository.findById(id);
    }

    public Coincidencia obtenerPorReportes(Long reportePerdidoId, Long reporteEncontradoId) {
        return coincidenciaRepository.findByReportePerdidoIdAndReporteEncontradoId(reportePerdidoId, reporteEncontradoId);
    }

    public List<Coincidencia> obtenerPorReporte(Long reporteId) {
        return coincidenciaRepository.findByReporteId(reporteId);
    }

    public List<Coincidencia> obtenerPendientes() {
        return coincidenciaRepository.findByEstadoOrderByPuntaje(Coincidencia.EstadoCoincidencia.PENDIENTE_REVISION);
    }

    public List<Coincidencia> obtenerConfirmadas() {
        return coincidenciaRepository.findConfirmedMatches();
    }

    public List<Coincidencia> obtenerPotenciales(Double puntajeMinimo) {
        return coincidenciaRepository.findPotentialMatches(puntajeMinimo);
    }

    public List<Coincidencia> obtenerRecientes(Integer diasAtras) {
        return coincidenciaRepository.findRecentMatches(
            LocalDateTime.now().minusDays(diasAtras)
        );
    }

    public Coincidencia confirmarCoincidencia(Long id, Long usuarioId) {
        if (id == null) throw new IllegalArgumentException("ID no puede ser nulo");
        Optional<Coincidencia> opt = coincidenciaRepository.findById(id);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Coincidencia no encontrada");
        }

        Coincidencia coincidencia = opt.get();
        coincidencia.setEstado(Coincidencia.EstadoCoincidencia.CONFIRMADA);
        coincidencia.setFechaConfirmacion(LocalDateTime.now());
        coincidencia.setUsuarioQuienConfirmo(usuarioId);

        // Notificar a usuarios (implementar en futuro)
        log.info("Coincidencia {} confirmada por usuario {}", id, usuarioId);

        return coincidenciaRepository.save(coincidencia);
    }

    public Coincidencia rechazarCoincidencia(Long id, String motivo) {
        if (id == null) throw new IllegalArgumentException("ID no puede ser nulo");
        Optional<Coincidencia> opt = coincidenciaRepository.findById(id);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Coincidencia no encontrada");
        }

        Coincidencia coincidencia = opt.get();
        coincidencia.setEstado(Coincidencia.EstadoCoincidencia.RECHAZADA);
        coincidencia.setMotivoRechazo(motivo);

        return coincidenciaRepository.save(coincidencia);
    }

    public Coincidencia resolverCaso(Long id) {
        if (id == null) throw new IllegalArgumentException("ID no puede ser nulo");
        Optional<Coincidencia> opt = coincidenciaRepository.findById(id);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Coincidencia no encontrada");
        }

        Coincidencia coincidencia = opt.get();
        coincidencia.setEstado(Coincidencia.EstadoCoincidencia.RESOLVIO_CASO);
        coincidencia.setFechaResolucion(LocalDateTime.now());

        return coincidenciaRepository.save(coincidencia);
    }

    public void eliminarCoincidencia(Long id) {
        if (id != null) {
            coincidenciaRepository.deleteById(id);
        }
    }
}