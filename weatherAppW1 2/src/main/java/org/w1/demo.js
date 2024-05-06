
const cachedWeatherData = localStorage.getItem('weatherData');
if (cachedWeatherData) {
    const data = JSON.parse(cachedWeatherData);
    displayWeatherData(data);
} else {
    fetch('http://localhost:8080/weather')
        .then(response => response.json())
        .then(data => {
            localStorage.setItem('weatherData', JSON.stringify(data));
            displayWeatherData(data);
        })
        .catch(error => console.error('Error:', error));
}
    function displayWeatherData(data) {
    let weatherDataDiv = document.getElementById('weatherData');
    weatherDataDiv.innerHTML = '';
            data.forEach(entry => {
                let city = entry.city;
                let temperature = entry.temperature;
                let pressure = entry.pressure;
                weatherDataDiv.innerHTML += `<p>City: ${city}, Temperature: ${temperature}, Pressure: ${pressure}</p>`;
            });
}