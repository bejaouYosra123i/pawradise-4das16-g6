const mongoose = require('mongoose');

const schema = new mongoose.Schema({
  name: { type: String, required: true, unique: true, trim: true }, // Nourriture, Couvertures, Médicaments, Équipements
  description: { type: String, trim: true },
}, { timestamps: true });

module.exports = mongoose.model('ResourceCategory', schema);
