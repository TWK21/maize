var express = require('express');
var app = express();

var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: false }));

var low = require('lowdb');
var FileSync = require('lowdb/adapters/FileSync');

var adapter = new FileSync('db.json');
var db = low(adapter);

db.defaults({
    catalog: []
}).write();

app.post('/api/search', (req, res) => {
    // query (string)

    var query = req.body.query;
    var results = db.get('catalog').filter((elem) => {
        console.log(elem.name.toLowerCase(), query.toLowerCase());
        return elem.name.toLowerCase().indexOf(query.toLowerCase()) >= 0;
    });
    console.log(results.value());
    res.json(results.value());
});

app.post('/api/id', (req, res) => {
    // id (int)

    var id = parseInt(req.body.id);
    var item = db.get('catalog').find({ id: id }).value();
    res.json(item);
});

app.listen(4600);
