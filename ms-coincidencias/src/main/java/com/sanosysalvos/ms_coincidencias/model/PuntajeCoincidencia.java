package com.sanosysalvos.ms_coincidencias.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PuntajeCoincidencia {
    private Double especie; // 0-100, peso alto
    private Double raza; // 0-100, peso 30%
    private Double color; // 0-100 (si aplica), peso 25%
    private Double tamaño; // 0-100 (si aplica), peso 20%
    private Double cercaniaGeografica; // 0-100, basado en distancia, peso 25%
    private Double proximidadFechas; // 0-100, basado en días, peso 15%

    public Double calcularPuntajeTotal() {
        Double total = 0.0;
        Double pesoEspecie = 1.0; // 100%
        Double pesoRaza = 0.30;
        Double pesoColor = 0.25;
        Double pesoTamaño = 0.20;
        Double pesoCercaniaGeografica = 0.25;
        Double pesoProximidadFechas = 0.15;

        if (especie != null) {
            total += especie * pesoEspecie;
        }
        if (raza != null) {
            total += raza * pesoRaza;
        }
        if (color != null) {
            total += color * pesoColor;
        }
        if (tamaño != null) {
            total += tamaño * pesoTamaño;
        }
        if (cercaniaGeografica != null) {
            total += cercaniaGeografica * pesoCercaniaGeografica;
        }
        if (proximidadFechas != null) {
            total += proximidadFechas * pesoProximidadFechas;
        }

        // Normalizar a 0-100 considerando que el máximo posible es:
        Double maxPosible = pesoEspecie + pesoRaza + pesoColor + pesoTamaño + pesoCercaniaGeografica + pesoProximidadFechas;
        return (total / maxPosible) * 100;
    }
}
