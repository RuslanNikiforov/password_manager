package ruslan.password_manager.services;

public interface EmailService {
    public void sendEmail(String to, String subject, String text);
}
