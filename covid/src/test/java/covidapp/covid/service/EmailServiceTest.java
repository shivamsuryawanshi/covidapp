package covidapp.covid.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------------------------------------------------------
    // 1️⃣ TEST SUCCESSFUL EMAIL SENDING
    // ---------------------------------------------------------
    @Test
    void testSendOtpEmailSuccess() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        assertDoesNotThrow(() -> emailService.sendOtpEmail("test@mail.com", "123456"));

        // Capture the message sent
        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();

        assertEquals("test@mail.com", sentMessage.getTo()[0]);
        assertEquals("Your OTP for COVID Dashboard Login", sentMessage.getSubject());
        assertTrue(sentMessage.getText().contains("123456"));
    }

    // ---------------------------------------------------------
    // 2️⃣ TEST FAILURE (JavaMailSender throws an exception)
    // ---------------------------------------------------------
    @Test
    void testSendOtpEmailFailure() {
        doThrow(new RuntimeException("SMTP error"))
                .when(mailSender)
                .send(any(SimpleMailMessage.class));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                emailService.sendOtpEmail("test@mail.com", "123456")
        );

        assertTrue(ex.getMessage().contains("Failed to send OTP email"));
    }
}
