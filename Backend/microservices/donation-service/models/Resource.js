const mongoose = require('mongoose');

const schema = new mongoose.Schema({
  name: { type: String, required: true, trim: true },
  description: { type: String, trim: true },
  categoryId: { type: mongoose.Schema.Types.ObjectId, ref: 'ResourceCategory', required: true },
  unit: { type: String, default: 'unité' }, // kg, boîte, unité...
  active: { type: Boolean, default: true },
}, { timestamps: true });

module.exports = mongoose.model('Resource', schema);
