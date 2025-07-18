package com.pahappa.hospital.converters;

import com.pahappa.hospital.models.Doctor;
import com.pahappa.hospital.services.doctor.DoctorService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

@FacesConverter("doctorConverter")
public class DoctorConverter implements Converter {
    @Inject
    private DoctorService doctorService;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        Long id = Long.parseLong(s);
        return !s.isEmpty() ? doctorService.getDoctorById(id) : null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {

        return (o instanceof Doctor)? String.valueOf(((Doctor) o).getDoctorId()) : "";
    }
}
