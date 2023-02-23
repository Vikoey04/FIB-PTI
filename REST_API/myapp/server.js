const express = require('express')
const fs = require('fs');
const app = express()
const port = 8080

app.use(express.json()); //Understand JSON input

app.get('/', (req, res) => {
  res.send('Hello World!')
})

app.post('/newrental', (req, res, next) => {
  if (fs.existsSync('rentals.json')) { 
    rentalsFileRawData = fs.readFileSync('rentals.json');
    rentalsJSON = JSON.parse(rentalsFileRawData); 
  }
  else { rentalsJSON = {"rentals": []}; }

  rentalsJSON['rentals'].push({"brand": req.body.brand,"model": req.body.model,"days": req.body.days});
  fs.writeFileSync("rentals.json", JSON.stringify(rentalsJSON));
  res.status(201);
  res.end(); 
})

app.get("/listrentals", (req, res, next) => {
  rentalsFileRawData = fs.readFileSync('rentals.json');
  rentalsJSON = JSON.parse(rentalsFileRawData);
  res.json(rentalsJSON);
});

app.listen(port, () => {
  console.log(`PTI HTTP Server listening at http://localhost:${port}`)
})


   
