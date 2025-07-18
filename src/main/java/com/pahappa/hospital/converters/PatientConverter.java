package com.pahappa.hospital.converters;

import com.pahappa.hospital.models.Patient;
import com.pahappa.hospital.services.patient.PatientService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

//this class is used to convert Patient objects to and from String representations in JSF
@FacesConverter(value = "patientConverter", managed = true)
public class PatientConverter implements Converter<Patient> {

    @Override
    public Patient getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) return null;
        PatientService patientService = context.getApplication()
                .evaluateExpressionGet(context, "#{patientService}", PatientService.class);
        return patientService.getPatientById(Long.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Patient patient) {
        if (patient == null) return "";
        return String.valueOf(patient.getPatientId());
    }
}
