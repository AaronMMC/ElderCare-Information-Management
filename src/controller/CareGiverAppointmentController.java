package controller;


import javafx.collections.ObservableList;
import model.AppointmentEntry;
import view.CaregiverAppointmentView;

public class CareGiverAppointmentController {

    private CaregiverAppointmentView view;

    public void CaregiverAppointmentController(CaregiverAppointmentView view) {
        this.view = view;
        initEventHandlers();
    }

    public CareGiverAppointmentController(CaregiverAppointmentView view) {
        this.view = view;
    }

    private void initEventHandlers() {
        view.getAcceptButton().setOnAction(e -> acceptAppointments());
        view.getCloseButton().setOnAction(e -> closeAppointments());
        view.getDeclineButton().setOnAction(e -> declineAppointments());
    }

    private void acceptAppointments() {
        ObservableList<AppointmentEntry> selectedAppointments = getSelectedAppointments();
        if (!selectedAppointments.isEmpty()) {
            // Call service or model logic here
            System.out.println("Accepted Appointments:");
            selectedAppointments.forEach(System.out::println);
        } else {
            System.out.println("No appointments selected to accept.");
        }
    }

    private void closeAppointments() {
        ObservableList<AppointmentEntry> selectedAppointments = getSelectedAppointments();
        if (!selectedAppointments.isEmpty()) {
            System.out.println("Closed Appointments:");
            selectedAppointments.forEach(System.out::println);
        } else {
            System.out.println("No appointments selected to close.");
        }
    }

    private void declineAppointments() {
        ObservableList<AppointmentEntry> selectedAppointments = getSelectedAppointments();
        if (!selectedAppointments.isEmpty()) {
            System.out.println("Declined Appointments:");
            selectedAppointments.forEach(System.out::println);
        } else {
            System.out.println("No appointments selected to decline.");
        }
    }

    private ObservableList<AppointmentEntry> getSelectedAppointments() {
        // Placeholder logic: You can extend AppointmentEntry with a `selected` property if needed
        // and filter those marked as selected. Right now CheckBoxTableCell is not wired to a property.
        // For actual selection logic, you may want to track `selected` state.
        return view.getAppointmentTable().getSelectionModel().getSelectedItems();
    }
}
