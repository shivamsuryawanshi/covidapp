# Email Configuration Guide

## Quick Setup Steps

### Option 1: Gmail (Recommended for Testing)

1. **Update `covid/src/main/resources/application.properties`**

   Find these lines:
   ```properties
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-app-password
   ```

2. **Replace with your Gmail credentials:**

   ```properties
   spring.mail.username=youractualemail@gmail.com
   spring.mail.password=abcd efgh ijkl mnop
   ```

3. **Get Gmail App Password:**
   - Go to: https://myaccount.google.com/security
   - Enable **2-Step Verification** (if not already enabled)
   - Click **"App passwords"** (under 2-Step Verification section)
   - Select app: **"Mail"**
   - Select device: **"Other (Custom name)"** → Type: "COVID App"
   - Click **"Generate"**
   - Copy the 16-character password (spaces don't matter)
   - Paste it in `application.properties` as `spring.mail.password`

### Option 2: Outlook/Hotmail

Update `application.properties`:
```properties
spring.mail.host=smtp-mail.outlook.com
spring.mail.port=587
spring.mail.username=your-email@outlook.com
spring.mail.password=your-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```

### Option 3: Yahoo Mail

Update `application.properties`:
```properties
spring.mail.host=smtp.mail.yahoo.com
spring.mail.port=587
spring.mail.username=your-email@yahoo.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```

### Option 4: Custom SMTP Server

If you have your own SMTP server, update:
```properties
spring.mail.host=your-smtp-server.com
spring.mail.port=587
spring.mail.username=your-username
spring.mail.password=your-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```

## Testing Email Configuration

After updating the configuration:

1. **Restart your Spring Boot application**
2. **Try signing up a new user** with a valid email address
3. **Check the console logs** - you should see: `"OTP email sent successfully to: [email]"`
4. **Check your email inbox** (and spam folder) for the OTP

## Troubleshooting

### Error: "Authentication failed"
- Make sure you're using an **App Password** (not your regular Gmail password)
- Verify 2-Step Verification is enabled
- Check that the email and password are correct

### Error: "Could not connect to SMTP host"
- Check your internet connection
- Verify the SMTP host and port are correct
- Some networks block SMTP ports - try a different network

### OTP not received
- Check spam/junk folder
- Verify email address is correct
- Check console logs for errors
- Make sure email service is properly configured

## Security Note

⚠️ **Never commit your email password to version control!**

Consider using environment variables:
```properties
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
```

Then set them in your system environment or IDE run configuration.

