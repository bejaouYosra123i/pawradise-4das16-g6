require('dotenv').config();
const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
const { registerWithEureka } = require('./eureka');
const registerRoutes = require('./routes');
const errorHandler = require('./middleware/errorHandler');

const app = express();
app.use(cors());
app.use(express.json());

app.get('/health', (req, res) => {
  res.json({
    status: 'UP',
    service: 'donation-service',
    mongo: mongoose.connection.readyState === 1 ? 'connected' : 'disconnected'
  });
});

registerRoutes(app);
app.use(errorHandler);

const PORT = process.env.PORT || 8086;

mongoose.connect(process.env.MONGO_URI)
  .then(() => {
    console.log('MongoDB connecté');
    app.listen(PORT, () => {
      console.log(`donation-service démarré sur le port ${PORT}`);
      registerWithEureka(PORT);
    });
  })
  .catch((err) => {
    console.error('Erreur MongoDB :', err.message);
    process.exit(1);
  });