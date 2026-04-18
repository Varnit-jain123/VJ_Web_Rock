// VJWebRock Auto-Generated Proxy (LoginService)

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

