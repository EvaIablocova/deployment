# Frontend Authentication Implementation Task

**Task ID:** T-6
**Priority:** High
**Epic:** Authentication & Security
**Status:** Ready for Development
**Created:** 2026-02-04
**Backend Status:** Complete

---

## Overview

Implement user authentication flow in the Flutter mobile application, integrating with the newly created Auth_microservice backend.

## Prerequisites

- Backend Auth API is deployed and running (port 9020)
- API Gateway is configured with JWT validation (port 8099)
- Flutter project has Riverpod for state management
- Flutter project has fpdart for error handling

## Dependencies to Add

```yaml
# pubspec.yaml
dependencies:
  flutter_secure_storage: ^9.0.0  # Secure token storage
  jwt_decoder: ^2.0.1             # JWT token parsing (optional)
  dio: ^5.4.0                     # HTTP client (if not using http package)
```

---

## API Endpoints

Base URL: `http://<server>:8099/api_gateway/auth`

### 1. Register User
```
POST /register
Content-Type: application/json

Request:
{
  "username": "string",
  "password": "string"
}

Response (201 Created):
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
  "username": "john",
  "userId": 1
}

Error (400 Bad Request):
{
  "message": "Username already exists"
}
```

### 2. Login
```
POST /login
Content-Type: application/json

Request:
{
  "username": "string",
  "password": "string"
}

Response (200 OK):
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
  "username": "john",
  "userId": 1
}

Error (401 Unauthorized):
{
  "message": "Invalid username or password"
}
```

### 3. Refresh Token
```
POST /refresh
Content-Type: application/json

Request:
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
}

Response (200 OK):
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
  "username": "john",
  "userId": 1
}

Error (401 Unauthorized):
{
  "message": "Invalid refresh token"
}
```

### 4. Protected Endpoints
All other `/api_gateway/*` endpoints require authentication:
```
GET /api_gateway/tasks
Authorization: Bearer <token>
```

---

## Implementation Tasks

### Task 1: Create Auth Models
**Estimated:** 1 hour

Create Freezed models for auth DTOs:

```dart
// lib/features/auth/models/login_request.dart
@freezed
class LoginRequest with _$LoginRequest {
  const factory LoginRequest({
    required String username,
    required String password,
  }) = _LoginRequest;

  factory LoginRequest.fromJson(Map<String, dynamic> json) =>
      _$LoginRequestFromJson(json);
}

// lib/features/auth/models/register_request.dart
@freezed
class RegisterRequest with _$RegisterRequest {
  const factory RegisterRequest({
    required String username,
    required String password,
  }) = _RegisterRequest;

  factory RegisterRequest.fromJson(Map<String, dynamic> json) =>
      _$RegisterRequestFromJson(json);
}

// lib/features/auth/models/auth_response.dart
@freezed
class AuthResponse with _$AuthResponse {
  const factory AuthResponse({
    required String token,
    required String refreshToken,
    required String username,
    required int userId,
  }) = _AuthResponse;

  factory AuthResponse.fromJson(Map<String, dynamic> json) =>
      _$AuthResponseFromJson(json);
}

// lib/features/auth/models/auth_state.dart
@freezed
class AuthState with _$AuthState {
  const factory AuthState.initial() = _Initial;
  const factory AuthState.loading() = _Loading;
  const factory AuthState.authenticated({
    required String token,
    required String refreshToken,
    required String username,
    required int userId,
  }) = _Authenticated;
  const factory AuthState.unauthenticated() = _Unauthenticated;
  const factory AuthState.error(String message) = _Error;
}
```

---

### Task 2: Create Token Storage Service
**Estimated:** 1 hour

Secure storage for JWT tokens:

```dart
// lib/features/auth/services/token_storage_service.dart
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class TokenStorageService {
  static const _storage = FlutterSecureStorage();
  static const _tokenKey = 'auth_token';
  static const _refreshTokenKey = 'refresh_token';
  static const _userIdKey = 'user_id';
  static const _usernameKey = 'username';

  Future<void> saveTokens({
    required String token,
    required String refreshToken,
    required int userId,
    required String username,
  }) async {
    await _storage.write(key: _tokenKey, value: token);
    await _storage.write(key: _refreshTokenKey, value: refreshToken);
    await _storage.write(key: _userIdKey, value: userId.toString());
    await _storage.write(key: _usernameKey, value: username);
  }

  Future<String?> getToken() async {
    return await _storage.read(key: _tokenKey);
  }

  Future<String?> getRefreshToken() async {
    return await _storage.read(key: _refreshTokenKey);
  }

  Future<int?> getUserId() async {
    final id = await _storage.read(key: _userIdKey);
    return id != null ? int.tryParse(id) : null;
  }

  Future<String?> getUsername() async {
    return await _storage.read(key: _usernameKey);
  }

  Future<void> clearTokens() async {
    await _storage.deleteAll();
  }

  Future<bool> hasValidToken() async {
    final token = await getToken();
    if (token == null) return false;
    // Optionally check expiration using jwt_decoder
    return true;
  }
}
```

---

### Task 3: Create Auth Repository
**Estimated:** 2 hours

```dart
// lib/features/auth/repositories/auth_repository.dart
import 'package:fpdart/fpdart.dart';

class AuthRepository {
  final HttpClient _client;
  final TokenStorageService _tokenStorage;

  static const _baseUrl = 'http://<server>:8099/api_gateway/auth';

  AuthRepository(this._client, this._tokenStorage);

  Future<Either<AuthFailure, AuthResponse>> register(RegisterRequest request) async {
    try {
      final response = await _client.post(
        '$_baseUrl/register',
        body: request.toJson(),
      );

      if (response.statusCode == 201) {
        final authResponse = AuthResponse.fromJson(response.data);
        await _tokenStorage.saveTokens(
          token: authResponse.token,
          refreshToken: authResponse.refreshToken,
          userId: authResponse.userId,
          username: authResponse.username,
        );
        return right(authResponse);
      } else {
        return left(AuthFailure.fromResponse(response));
      }
    } catch (e) {
      return left(AuthFailure.network(e.toString()));
    }
  }

  Future<Either<AuthFailure, AuthResponse>> login(LoginRequest request) async {
    try {
      final response = await _client.post(
        '$_baseUrl/login',
        body: request.toJson(),
      );

      if (response.statusCode == 200) {
        final authResponse = AuthResponse.fromJson(response.data);
        await _tokenStorage.saveTokens(
          token: authResponse.token,
          refreshToken: authResponse.refreshToken,
          userId: authResponse.userId,
          username: authResponse.username,
        );
        return right(authResponse);
      } else {
        return left(AuthFailure.invalidCredentials());
      }
    } catch (e) {
      return left(AuthFailure.network(e.toString()));
    }
  }

  Future<Either<AuthFailure, AuthResponse>> refreshToken() async {
    try {
      final refreshToken = await _tokenStorage.getRefreshToken();
      if (refreshToken == null) {
        return left(AuthFailure.noRefreshToken());
      }

      final response = await _client.post(
        '$_baseUrl/refresh',
        body: {'refreshToken': refreshToken},
      );

      if (response.statusCode == 200) {
        final authResponse = AuthResponse.fromJson(response.data);
        await _tokenStorage.saveTokens(
          token: authResponse.token,
          refreshToken: authResponse.refreshToken,
          userId: authResponse.userId,
          username: authResponse.username,
        );
        return right(authResponse);
      } else {
        await _tokenStorage.clearTokens();
        return left(AuthFailure.sessionExpired());
      }
    } catch (e) {
      return left(AuthFailure.network(e.toString()));
    }
  }

  Future<void> logout() async {
    await _tokenStorage.clearTokens();
  }

  Future<bool> isAuthenticated() async {
    return await _tokenStorage.hasValidToken();
  }
}
```

---

### Task 4: Create Auth Provider (Riverpod)
**Estimated:** 2 hours

```dart
// lib/features/auth/providers/auth_provider.dart
import 'package:riverpod_annotation/riverpod_annotation.dart';

part 'auth_provider.g.dart';

@riverpod
class Auth extends _$Auth {
  @override
  AuthState build() {
    _checkAuthStatus();
    return const AuthState.initial();
  }

  Future<void> _checkAuthStatus() async {
    final repository = ref.read(authRepositoryProvider);
    final isAuthenticated = await repository.isAuthenticated();

    if (isAuthenticated) {
      final tokenStorage = ref.read(tokenStorageProvider);
      final username = await tokenStorage.getUsername();
      final userId = await tokenStorage.getUserId();
      final token = await tokenStorage.getToken();
      final refreshToken = await tokenStorage.getRefreshToken();

      if (username != null && userId != null && token != null && refreshToken != null) {
        state = AuthState.authenticated(
          token: token,
          refreshToken: refreshToken,
          username: username,
          userId: userId,
        );
        return;
      }
    }
    state = const AuthState.unauthenticated();
  }

  Future<void> login(String username, String password) async {
    state = const AuthState.loading();

    final repository = ref.read(authRepositoryProvider);
    final result = await repository.login(
      LoginRequest(username: username, password: password),
    );

    result.fold(
      (failure) => state = AuthState.error(failure.message),
      (response) => state = AuthState.authenticated(
        token: response.token,
        refreshToken: response.refreshToken,
        username: response.username,
        userId: response.userId,
      ),
    );
  }

  Future<void> register(String username, String password) async {
    state = const AuthState.loading();

    final repository = ref.read(authRepositoryProvider);
    final result = await repository.register(
      RegisterRequest(username: username, password: password),
    );

    result.fold(
      (failure) => state = AuthState.error(failure.message),
      (response) => state = AuthState.authenticated(
        token: response.token,
        refreshToken: response.refreshToken,
        username: response.username,
        userId: response.userId,
      ),
    );
  }

  Future<void> logout() async {
    final repository = ref.read(authRepositoryProvider);
    await repository.logout();
    state = const AuthState.unauthenticated();
  }

  Future<void> refreshToken() async {
    final repository = ref.read(authRepositoryProvider);
    final result = await repository.refreshToken();

    result.fold(
      (failure) {
        state = const AuthState.unauthenticated();
      },
      (response) => state = AuthState.authenticated(
        token: response.token,
        refreshToken: response.refreshToken,
        username: response.username,
        userId: response.userId,
      ),
    );
  }
}
```

---

### Task 5: Create HTTP Interceptor for Auth
**Estimated:** 1 hour

Auto-attach token and handle 401 responses:

```dart
// lib/core/network/auth_interceptor.dart
class AuthInterceptor extends Interceptor {
  final TokenStorageService _tokenStorage;
  final Ref _ref;

  AuthInterceptor(this._tokenStorage, this._ref);

  @override
  void onRequest(RequestOptions options, RequestInterceptorHandler handler) async {
    // Skip auth header for auth endpoints
    if (options.path.contains('/auth/')) {
      return handler.next(options);
    }

    final token = await _tokenStorage.getToken();
    if (token != null) {
      options.headers['Authorization'] = 'Bearer $token';
    }
    handler.next(options);
  }

  @override
  void onError(DioException err, ErrorInterceptorHandler handler) async {
    if (err.response?.statusCode == 401) {
      // Try to refresh token
      final authNotifier = _ref.read(authProvider.notifier);
      await authNotifier.refreshToken();

      // Check if refresh was successful
      final authState = _ref.read(authProvider);
      if (authState is _Authenticated) {
        // Retry the request with new token
        final options = err.requestOptions;
        options.headers['Authorization'] = 'Bearer ${authState.token}';

        try {
          final response = await Dio().fetch(options);
          return handler.resolve(response);
        } catch (e) {
          return handler.next(err);
        }
      }
    }
    handler.next(err);
  }
}
```

---

### Task 6: Create Login Screen
**Estimated:** 3 hours

```dart
// lib/features/auth/screens/login_screen.dart
class LoginScreen extends ConsumerStatefulWidget {
  const LoginScreen({super.key});

  @override
  ConsumerState<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends ConsumerState<LoginScreen> {
  final _formKey = GlobalKey<FormState>();
  final _usernameController = TextEditingController();
  final _passwordController = TextEditingController();
  bool _obscurePassword = true;

  @override
  Widget build(BuildContext context) {
    final authState = ref.watch(authProvider);

    ref.listen(authProvider, (previous, next) {
      next.maybeWhen(
        authenticated: (_, __, ___, ____) {
          Navigator.of(context).pushReplacementNamed('/home');
        },
        error: (message) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text(message), backgroundColor: Colors.red),
          );
        },
        orElse: () {},
      );
    });

    return Scaffold(
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(24.0),
          child: Form(
            key: _formKey,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                // Logo/Title
                const Text(
                  'HouseMate',
                  style: TextStyle(
                    fontSize: 32,
                    fontWeight: FontWeight.bold,
                  ),
                  textAlign: TextAlign.center,
                ),
                const SizedBox(height: 8),
                const Text(
                  'Welcome back!',
                  style: TextStyle(fontSize: 16, color: Colors.grey),
                  textAlign: TextAlign.center,
                ),
                const SizedBox(height: 48),

                // Username Field
                TextFormField(
                  controller: _usernameController,
                  decoration: const InputDecoration(
                    labelText: 'Username',
                    prefixIcon: Icon(Icons.person),
                    border: OutlineInputBorder(),
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return 'Please enter your username';
                    }
                    return null;
                  },
                ),
                const SizedBox(height: 16),

                // Password Field
                TextFormField(
                  controller: _passwordController,
                  obscureText: _obscurePassword,
                  decoration: InputDecoration(
                    labelText: 'Password',
                    prefixIcon: const Icon(Icons.lock),
                    border: const OutlineInputBorder(),
                    suffixIcon: IconButton(
                      icon: Icon(
                        _obscurePassword ? Icons.visibility : Icons.visibility_off,
                      ),
                      onPressed: () {
                        setState(() => _obscurePassword = !_obscurePassword);
                      },
                    ),
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return 'Please enter your password';
                    }
                    return null;
                  },
                ),
                const SizedBox(height: 24),

                // Login Button
                ElevatedButton(
                  onPressed: authState.maybeWhen(
                    loading: () => null,
                    orElse: () => _handleLogin,
                  ),
                  style: ElevatedButton.styleFrom(
                    padding: const EdgeInsets.symmetric(vertical: 16),
                  ),
                  child: authState.maybeWhen(
                    loading: () => const SizedBox(
                      height: 20,
                      width: 20,
                      child: CircularProgressIndicator(strokeWidth: 2),
                    ),
                    orElse: () => const Text('Login'),
                  ),
                ),
                const SizedBox(height: 16),

                // Register Link
                TextButton(
                  onPressed: () {
                    Navigator.of(context).pushNamed('/register');
                  },
                  child: const Text("Don't have an account? Register"),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  void _handleLogin() {
    if (_formKey.currentState!.validate()) {
      ref.read(authProvider.notifier).login(
        _usernameController.text.trim(),
        _passwordController.text,
      );
    }
  }

  @override
  void dispose() {
    _usernameController.dispose();
    _passwordController.dispose();
    super.dispose();
  }
}
```

---

### Task 7: Create Register Screen
**Estimated:** 3 hours

```dart
// lib/features/auth/screens/register_screen.dart
class RegisterScreen extends ConsumerStatefulWidget {
  const RegisterScreen({super.key});

  @override
  ConsumerState<RegisterScreen> createState() => _RegisterScreenState();
}

class _RegisterScreenState extends ConsumerState<RegisterScreen> {
  final _formKey = GlobalKey<FormState>();
  final _usernameController = TextEditingController();
  final _passwordController = TextEditingController();
  final _confirmPasswordController = TextEditingController();
  bool _obscurePassword = true;
  bool _obscureConfirmPassword = true;

  @override
  Widget build(BuildContext context) {
    final authState = ref.watch(authProvider);

    ref.listen(authProvider, (previous, next) {
      next.maybeWhen(
        authenticated: (_, __, ___, ____) {
          Navigator.of(context).pushReplacementNamed('/home');
        },
        error: (message) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text(message), backgroundColor: Colors.red),
          );
        },
        orElse: () {},
      );
    });

    return Scaffold(
      appBar: AppBar(title: const Text('Create Account')),
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(24.0),
          child: Form(
            key: _formKey,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                const SizedBox(height: 24),

                // Username Field
                TextFormField(
                  controller: _usernameController,
                  decoration: const InputDecoration(
                    labelText: 'Username',
                    prefixIcon: Icon(Icons.person),
                    border: OutlineInputBorder(),
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return 'Please enter a username';
                    }
                    if (value.length < 3) {
                      return 'Username must be at least 3 characters';
                    }
                    return null;
                  },
                ),
                const SizedBox(height: 16),

                // Password Field
                TextFormField(
                  controller: _passwordController,
                  obscureText: _obscurePassword,
                  decoration: InputDecoration(
                    labelText: 'Password',
                    prefixIcon: const Icon(Icons.lock),
                    border: const OutlineInputBorder(),
                    suffixIcon: IconButton(
                      icon: Icon(
                        _obscurePassword ? Icons.visibility : Icons.visibility_off,
                      ),
                      onPressed: () {
                        setState(() => _obscurePassword = !_obscurePassword);
                      },
                    ),
                  ),
                  validator: _validatePassword,
                ),
                const SizedBox(height: 16),

                // Confirm Password Field
                TextFormField(
                  controller: _confirmPasswordController,
                  obscureText: _obscureConfirmPassword,
                  decoration: InputDecoration(
                    labelText: 'Confirm Password',
                    prefixIcon: const Icon(Icons.lock_outline),
                    border: const OutlineInputBorder(),
                    suffixIcon: IconButton(
                      icon: Icon(
                        _obscureConfirmPassword ? Icons.visibility : Icons.visibility_off,
                      ),
                      onPressed: () {
                        setState(() => _obscureConfirmPassword = !_obscureConfirmPassword);
                      },
                    ),
                  ),
                  validator: (value) {
                    if (value != _passwordController.text) {
                      return 'Passwords do not match';
                    }
                    return null;
                  },
                ),
                const SizedBox(height: 8),

                // Password Requirements
                const Text(
                  'Password must contain:\n'
                  '• At least 8 characters\n'
                  '• At least 1 number\n'
                  '• At least 1 special character',
                  style: TextStyle(fontSize: 12, color: Colors.grey),
                ),
                const SizedBox(height: 24),

                // Register Button
                ElevatedButton(
                  onPressed: authState.maybeWhen(
                    loading: () => null,
                    orElse: () => _handleRegister,
                  ),
                  style: ElevatedButton.styleFrom(
                    padding: const EdgeInsets.symmetric(vertical: 16),
                  ),
                  child: authState.maybeWhen(
                    loading: () => const SizedBox(
                      height: 20,
                      width: 20,
                      child: CircularProgressIndicator(strokeWidth: 2),
                    ),
                    orElse: () => const Text('Create Account'),
                  ),
                ),
                const SizedBox(height: 16),

                // Login Link
                TextButton(
                  onPressed: () {
                    Navigator.of(context).pop();
                  },
                  child: const Text('Already have an account? Login'),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  String? _validatePassword(String? value) {
    if (value == null || value.isEmpty) {
      return 'Please enter a password';
    }
    if (value.length < 8) {
      return 'Password must be at least 8 characters';
    }
    if (!RegExp(r'[0-9]').hasMatch(value)) {
      return 'Password must contain at least 1 number';
    }
    if (!RegExp(r'[!@#$%^&*(),.?":{}|<>]').hasMatch(value)) {
      return 'Password must contain at least 1 special character';
    }
    return null;
  }

  void _handleRegister() {
    if (_formKey.currentState!.validate()) {
      ref.read(authProvider.notifier).register(
        _usernameController.text.trim(),
        _passwordController.text,
      );
    }
  }

  @override
  void dispose() {
    _usernameController.dispose();
    _passwordController.dispose();
    _confirmPasswordController.dispose();
    super.dispose();
  }
}
```

---

### Task 8: Update App Routing
**Estimated:** 1 hour

```dart
// lib/app/router.dart
class AppRouter {
  static Route<dynamic> generateRoute(RouteSettings settings) {
    switch (settings.name) {
      case '/':
      case '/login':
        return MaterialPageRoute(builder: (_) => const LoginScreen());
      case '/register':
        return MaterialPageRoute(builder: (_) => const RegisterScreen());
      case '/home':
        return MaterialPageRoute(builder: (_) => const HomeScreen());
      default:
        return MaterialPageRoute(
          builder: (_) => Scaffold(
            body: Center(child: Text('No route defined for ${settings.name}')),
          ),
        );
    }
  }
}

// lib/app/app.dart - Add auth wrapper
class App extends ConsumerWidget {
  const App({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final authState = ref.watch(authProvider);

    return MaterialApp(
      title: 'HouseMate',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        useMaterial3: true,
      ),
      onGenerateRoute: AppRouter.generateRoute,
      home: authState.maybeWhen(
        initial: () => const SplashScreen(),
        loading: () => const SplashScreen(),
        authenticated: (_, __, ___, ____) => const HomeScreen(),
        orElse: () => const LoginScreen(),
      ),
    );
  }
}
```

---

### Task 9: Create Splash Screen
**Estimated:** 1 hour

```dart
// lib/features/auth/screens/splash_screen.dart
class SplashScreen extends StatelessWidget {
  const SplashScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            // App Logo
            Icon(Icons.home, size: 80, color: Colors.blue),
            SizedBox(height: 24),
            Text(
              'HouseMate',
              style: TextStyle(
                fontSize: 28,
                fontWeight: FontWeight.bold,
              ),
            ),
            SizedBox(height: 48),
            CircularProgressIndicator(),
          ],
        ),
      ),
    );
  }
}
```

---

## File Structure

```
lib/
├── features/
│   └── auth/
│       ├── models/
│       │   ├── login_request.dart
│       │   ├── register_request.dart
│       │   ├── auth_response.dart
│       │   ├── auth_state.dart
│       │   └── auth_failure.dart
│       ├── repositories/
│       │   └── auth_repository.dart
│       ├── services/
│       │   └── token_storage_service.dart
│       ├── providers/
│       │   └── auth_provider.dart
│       └── screens/
│           ├── login_screen.dart
│           ├── register_screen.dart
│           └── splash_screen.dart
├── core/
│   └── network/
│       └── auth_interceptor.dart
└── app/
    ├── app.dart
    └── router.dart
```

---

## Acceptance Criteria

### Functional Requirements
- [ ] User can register with username and password
- [ ] User can login with valid credentials
- [ ] User sees error message for invalid credentials
- [ ] User sees error message for duplicate username on register
- [ ] JWT token is stored securely after login/register
- [ ] User remains logged in after app restart
- [ ] User can logout and is redirected to login screen
- [ ] Protected API calls include Bearer token
- [ ] Token is automatically refreshed when expired
- [ ] User is redirected to login when refresh fails

### UI Requirements
- [ ] Login screen with username/password fields
- [ ] Register screen with password confirmation
- [ ] Password visibility toggle
- [ ] Form validation with error messages
- [ ] Loading indicator during API calls
- [ ] Splash screen while checking auth status

### Non-Functional Requirements
- [ ] Tokens stored using flutter_secure_storage (encrypted)
- [ ] No sensitive data in logs
- [ ] Graceful error handling for network failures

---

## Testing Checklist

### Manual Testing
1. [ ] Register new user - success
2. [ ] Register with existing username - error shown
3. [ ] Login with valid credentials - redirected to home
4. [ ] Login with invalid credentials - error shown
5. [ ] Close app and reopen - remains logged in
6. [ ] Logout - redirected to login
7. [ ] Access protected endpoint - works with token
8. [ ] Wait for token expiry - auto-refresh works
9. [ ] Force invalid refresh token - redirected to login

### API Testing (Backend Verification)
```bash
# Register
curl -X POST http://localhost:8099/api_gateway/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "Test123!@#"}'

# Login
curl -X POST http://localhost:8099/api_gateway/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "Test123!@#"}'

# Protected endpoint (use token from login response)
curl http://localhost:8099/api_gateway/tasks \
  -H "Authorization: Bearer <token>"
```

---

## Estimated Total Effort

| Task | Hours |
|------|-------|
| Task 1: Auth Models | 1 |
| Task 2: Token Storage | 1 |
| Task 3: Auth Repository | 2 |
| Task 4: Auth Provider | 2 |
| Task 5: HTTP Interceptor | 1 |
| Task 6: Login Screen | 3 |
| Task 7: Register Screen | 3 |
| Task 8: App Routing | 1 |
| Task 9: Splash Screen | 1 |
| Testing & Polish | 2 |
| **Total** | **17 hours** |

---

## Notes for Developers

1. **Server URL Configuration**: Store base URL in environment config, not hardcoded
2. **Error Messages**: Use user-friendly messages, log technical details
3. **Token Expiry**: Access token expires in 1 hour, refresh token in 7 days
4. **Biometric Auth**: Consider adding fingerprint/face ID for future enhancement
5. **Remember Me**: Optional - can skip refresh token storage if unchecked

---

## Related Documentation

- Backend Auth Implementation: `Auth_microservice/`
- API Gateway Security: `API_Gateway/src/main/java/.../config/SecurityConfig.java`
- Architecture Diagram: `architecture.html`
- Project Plan: `plan.txt`
