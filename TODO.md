# MVC Refactor - Progress

## Planned steps
1. Create MVC core for Auth (Model/DAO/Service/Controller):
   - src/model/User.java
   - src/dao/UserDAO.java
   - src/service/AuthService.java
   - src/controller/AuthController.java
2. Refactor View: LoginForm
   - Remove SQL/DB logic
   - Call AuthController on login
   - Open MainDashboard with returned User
3. Refactor View: RegisterForm
   - Remove SQL/DB logic
   - Call AuthController on register
4. Refactor View: MainDashboard
   - Remove direct DB query for fullname
   - Use passed User model (or fields already loaded)

## Completed
- [x] 1) Create MVC core for Auth
- [x] 2) Refactor LoginForm
- [x] 3) Refactor RegisterForm
- [x] 4) Refactor MainDashboard

