package cvb.capp.data.daos;

import cvb.capp.business.models.Appointment;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface AppointmentDAO {
    List<Appointment> fetchAllAppointmentsByDatePerTestCenter(Date date, int testCenterId);

    List<Appointment> fetchAllAppointmentsByDateAndHourPerTestCenter(Date date, int testCenterId, Time time);

    boolean addAppointment(Appointment appointment, int persons_id);

    List<Appointment> fetchAppointmentsForPerson(int personId);

    boolean checkForDoubleAppointments(Date date, int personId);

    List<Appointment> fetchAllAppointments();

    boolean deleteAppointment(int appointmentId);

    List<Appointment> fetchAllPresentAppointments(Date date);

    boolean removeAppointmentForPerson(int personId, int appointmentId);
}
