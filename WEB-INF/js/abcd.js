// VJWebRock Auto-Generated Proxy SDK

class SecurityChecker {
  constructor() {
    this.sessionScope = '';
  }
}

class Student {
  constructor() {
    this.rollNumber = 0;
    this.name = '';
    this.gender = '';
  }
}

class StudentServiceTable {
  update(data) {
    let url = 'schoolService/studentTable/update';
    return fetch(url, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(data)
    }).then(r => r.text());
  }

  getAll(data) {
    let url = 'schoolService/studentTable/getAll';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.json());
  }

  getBy(data) {
    let url = 'schoolService/studentTable/getBy';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.json());
  }

  delete(data) {
    let url = 'schoolService/studentTable/delete';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

  add(data) {
    let url = 'schoolService/studentTable/add';
    return fetch(url, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(data)
    }).then(r => r.text());
  }

}

class JsonTestService {
  add(data) {
    let url = 'schoolService/jsonTest/add';
    return fetch(url, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(data)
    }).then(r => r.text());
  }

  check(data) {
    let url = 'schoolService/jsonTest/checkSession';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

  save(data) {
    let url = 'schoolService/jsonTest/save';
    return fetch(url, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(data)
    }).then(r => r.text());
  }

}

class AutoWiredTestService {
  setup(data) {
    let url = 'schoolService/autowired/setup';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

  test(data) {
    let url = 'schoolService/autowired/test';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

}

class StudentService {
  details(data) {
    let url = 'schoolService/student/details';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

  add(data) {
    let url = 'schoolService/student/add';
    return fetch(url, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(data)
    }).then(r => r.text());
  }

  list(data) {
    let url = 'schoolService/student/list';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

}

class ScopeTestService {
  get(data) {
    let url = 'schoolService/scope/get';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

  set(data) {
    let url = 'schoolService/scope/set';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

}

class LoginService {
  login(data) {
    let url = 'schoolService/login/doLogin';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

  logout(data) {
    let url = 'schoolService/login/logout';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

}

class AdminService {
  list(data) {
    let url = 'schoolService/admin/list';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

  update(data) {
    let url = 'schoolService/admin/update';
    return fetch(url, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(data)
    }).then(r => r.text());
  }

}

class CalculatorService {
  add(data) {
    let url = 'schoolService/calculator/add';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

}

class InjectionTestService {
  print(data) {
    let url = 'schoolService/injection/print';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

}

class ParameterTestService {
  calculate(data) {
    let url = 'schoolService/parameter/calc';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

  booleanTest(data) {
    let url = 'schoolService/parameter/bool';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

  charTest(data) {
    let url = 'schoolService/parameter/charTest';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

  stringTest(data) {
    let url = 'schoolService/parameter/string';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

}

class ProtectedService {
  getData(data) {
    let url = 'schoolService/secure/data';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

}

