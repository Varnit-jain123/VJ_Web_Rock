/**
 * VJWebRock Auto-Generated Proxy SDK
 * Zero-Config Client for Bobby
 */

class VJWebRockClient {
  async _call(path, data, method) {
    const contextPath = window.location.pathname.split('/')[1];
    let url = `/${contextPath}/schoolService${path}`;
    const options = { method: method, headers: {} };

    if (method === 'POST') {
      options.headers['Content-Type'] = 'application/json';
      options.body = JSON.stringify(data);
    } else if (data) {
      const params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }

    const response = await fetch(url, options);
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) return await response.json();
    return await response.text();
  }
}

class JSGeneratorServlet {
  constructor() {
  }
}

class ApplicationDirectory {
  constructor() {
    this.directory = null;
  }
}

class JSLoaderServlet {
  constructor() {
  }
}

class VJWebRock {
  constructor() {
  }
}

class Service {
  constructor() {
    this.serviceClass = null;
    this.path = null;
    this.service = null;
    this.isGetAllowed = false;
    this.isPostAllowed = false;
    this.forwardTo = null;
    this.runOnStartup = false;
    this.priority = 0;
    this.autoWiredFields = null;
    this.injectRequestParameterFields = null;
  }
}

class SecurityChecker {
  constructor() {
    this.sessionScope = null;
  }
}

class webRockModel {
  constructor() {
    this.map = null;
  }
}

class ApplicationScope {
  constructor() {
    this.servletContext = null;
  }
}

class VJWebRockStarter {
  constructor() {
  }
}

class Student {
  constructor() {
    this.rollNumber = 0;
    this.name = null;
    this.gender = null;
  }
}

class RequestScope {
  constructor() {
    this.request = null;
  }
}

class SessionScope {
  constructor() {
    this.session = null;
  }
}

class ScopeTestService extends VJWebRockClient {
  get(data) {
    return this._call('/scope/get', data, 'GET');
  }
  set(data) {
    return this._call('/scope/set', data, 'GET');
  }
}

class JsonTestService extends VJWebRockClient {
  add(data) {
    return this._call('/jsonTest/add', data, 'POST');
  }
  check(data) {
    return this._call('/jsonTest/checkSession', data, 'GET');
  }
  save(data) {
    return this._call('/jsonTest/save', data, 'POST');
  }
}

class CalculatorService extends VJWebRockClient {
  add(data) {
    return this._call('/calculator/add', data, 'GET');
  }
}

class AdminService extends VJWebRockClient {
  list(data) {
    return this._call('/admin/list', data, 'GET');
  }
  update(data) {
    return this._call('/admin/update', data, 'POST');
  }
}

class ProtectedService extends VJWebRockClient {
  getData(data) {
    return this._call('/secure/data', data, 'GET');
  }
}

class StudentService extends VJWebRockClient {
  details(data) {
    return this._call('/student/details', data, 'GET');
  }
  add(data) {
    return this._call('/student/add', data, 'POST');
  }
  list(data) {
    return this._call('/student/list', data, 'GET');
  }
}

class AutoWiredTestService extends VJWebRockClient {
  setup(data) {
    return this._call('/autowired/setup', data, 'GET');
  }
  test(data) {
    return this._call('/autowired/test', data, 'GET');
  }
}

class StudentServiceTable extends VJWebRockClient {
  update(data) {
    return this._call('/studentTable/update', data, 'POST');
  }
  getAll(data) {
    return this._call('/studentTable/getAll', data, 'GET');
  }
  getBy(data) {
    return this._call('/studentTable/getBy', data, 'GET');
  }
  delete(data) {
    return this._call('/studentTable/delete', data, 'GET');
  }
  add(data) {
    return this._call('/studentTable/add', data, 'POST');
  }
}

class InjectionTestService extends VJWebRockClient {
  print(data) {
    return this._call('/injection/print', data, 'GET');
  }
}

class LoginService extends VJWebRockClient {
  login(data) {
    return this._call('/login/doLogin', data, 'GET');
  }
  logout(data) {
    return this._call('/login/logout', data, 'GET');
  }
}

class ParameterTestService extends VJWebRockClient {
  calculate(data) {
    return this._call('/parameter/calc', data, 'GET');
  }
  booleanTest(data) {
    return this._call('/parameter/bool', data, 'GET');
  }
  charTest(data) {
    return this._call('/parameter/charTest', data, 'GET');
  }
  stringTest(data) {
    return this._call('/parameter/string', data, 'GET');
  }
}

// Global Service Registry
window.remote = {
  ScopeTestService: new ScopeTestService(),
  JsonTestService: new JsonTestService(),
  CalculatorService: new CalculatorService(),
  AdminService: new AdminService(),
  ProtectedService: new ProtectedService(),
  StudentService: new StudentService(),
  AutoWiredTestService: new AutoWiredTestService(),
  StudentServiceTable: new StudentServiceTable(),
  InjectionTestService: new InjectionTestService(),
  LoginService: new LoginService(),
  ParameterTestService: new ParameterTestService()
};
