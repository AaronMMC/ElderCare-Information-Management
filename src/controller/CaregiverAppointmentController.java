//package controller;
//
//import javafx.collections.ObservableList;
//import javafx.scene.control.Alert;
//import javafx.scene.control.TableView;
//import model.AppointmentEntry;
//import view.CaregiverAppointmentView;
//
//public class CaregiverAppointmentController {
//
//    private final CaregiverAppointmentView view;
//
//    public CaregiverAppointmentController(CaregiverAppointmentView view) {
//        this.view = view;
//        attachEventHandlers();
//    }
//
//    private void attachEventHandlers() {
//        view.getAcceptButton().setOnAction(e -> handleAcceptAppointments());
//        view.getCloseButton().setOnAction(e -> handleCloseAppointments());
//        view.getDeclineButton().setOnAction(e -> handleDeclineAppointments());
//    }
//
//    private void handleAcceptAppointments() {
//        ObservableList<AppointmentEntry> selectedAppointments = getSelectedAppointments();
//        if (selectedAppointments.isEmpty()) {
//            showAlert("No appointments selected to accept.");
//        } else {
//            for (AppointmentEntry entry : selectedAppointments) {
//                System.out.println("Accepted: " + entry.getGuardianName());
//                // Optional: Update model status here
//            }
//            showAlert("Accepted " + selectedAppointments.size() + " appointment(s).");
//        }
//    }
//
//    private void handleCloseAppointments() {
//        ObservableList<AppointmentEntry> selectedAppointments = getSelectedAppointments();
//        if (selectedAppointments.isEmpty()) {
//            showAlert("No appointments selected to close.");
//        } else {
//            for (AppointmentEntry entry : selectedAppointments) {
//                System.out.println("Closed: " + entry.getGuardianName());
//                // Optional: Update model status here
//            }
//            showAlert("Closed " + selectedAppointments.size() + " appointment(s).");
//        }
//    }
//
//    private void handleDeclineAppointments() {
//        ObservableList<AppointmentEntry> selectedAppointments = getSelectedAppointments();
//        if (selectedAppointments.isEmpty()) {
//            showAlert("No appointments selected to decline.");
//        } else {
//            for (AppointmentEntry entry : selectedAppointments) {
//                System.out.println("Declined: " + entry.getGuardianName());
//                // Optional: Update model status here
//            }
//            showAlert("Declined " + selectedAppointments.size() + " appointment(s).");
//        }
//    }
//
//    private ObservableList<AppointmentEntry> getSelectedAppointments() {
//        // For now, simulate selection. You might want to add a real 'selected' property.
//        TableView<AppointmentEntry> table = view.getAppointmentTable();
//        return table.getSelectionModel().getSelectedItems();
//    }
//
//    private void showAlert(String message) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Appointment Info");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//}
