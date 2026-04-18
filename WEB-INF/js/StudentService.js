// VJWebRock Auto-Generated Proxy (StudentService)

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

