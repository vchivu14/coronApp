package cvb.capp.data.daos;

import cvb.capp.business.models.AppointmentTemp;

public interface AppointmentTempDAO {
    AppointmentTemp findAppointmentTempById(int appointmentTempId);

    boolean deleteAppointmentTemp(int personId);

    int addAppointmentTemp(AppointmentTemp appointmentTemp);
}
