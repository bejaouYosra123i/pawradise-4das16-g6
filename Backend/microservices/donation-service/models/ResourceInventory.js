const mongoose = require('mongoose');

const schema = new mongoose.Schema({
  resourceId: { type: mongoose.Schema.Types.ObjectId, ref: 'Resource', required: true, unique: true },
  quantity: { type: Number, required: true, min: 0, default: 0 },
  minThreshold: { type: Number, default: 0, min: 0 }, // seuil d'alerte
  location: { type: String, trim: true },
}, { timestamps: true });

module.exports = mongoose.model('ResourceInventory', schema);
