# TODO: Implement Automatic Logout Functionality

## Backend Changes
- [ ] Add `logout.timeout.minutes` property to `application.properties` (default 30 minutes)
- [ ] Update `User` entity to include `logoutTimeoutMinutes` field (optional per-user setting)
- [ ] Modify `AuthController.verifyOtp()` to include timeout and login timestamp in response
- [ ] Add endpoint to update global/per-user timeout settings

## Frontend Changes
- [ ] Update `AuthContext` to store login timestamp and timeout
- [ ] Implement automatic logout timer using `setTimeout`
- [ ] Check session expiry on app load and logout if expired
- [ ] Handle logout on timer expiry or manual logout

## Testing
- [ ] Test login/logout flow with different timeout values
- [ ] Verify automatic logout works when timeout expires
- [ ] Ensure session expiry check works on page refresh
