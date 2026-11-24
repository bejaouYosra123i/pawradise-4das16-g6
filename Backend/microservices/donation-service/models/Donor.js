const mongoose = require('mongoose');

const schema = new mongoose.Schema({
  userId: { type: String, index: true }, // référence externe (optionnel : don anonyme possible)
  firstName: { type: String, required: true, trim: true },
  lastName: { type: String, required: true, trim: true },
  email: { type: String, required: true, lowercase: true, trim: true, unique: true },
  phone: { type: String },
  anonymous: { type: Boolean, default: false },
}, { timestamps: true });

module.exports = mongoose.model('Donor', schema);
