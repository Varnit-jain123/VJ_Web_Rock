// VJWebRock Auto-Generated Proxy (CalculatorService)

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

