const mongoose = require('mongoose');

const schema = new mongoose.Schema({
  donorId: { type: mongoose.Schema.Types.ObjectId, ref: 'Donor', required: true },
  targetType: { type: String, enum: ['ANIMAL', 'ORGANIZATION'], required: true },
  animalId: { type: String, required: function () { return this.targetType === 'ANIMAL'; } },       // externe
  shelterId: { type: String, required: function () { return this.targetType === 'ORGANIZATION'; } }, // externe
  amount: { type: Number, required: true, min: 1 },
  currency: { type: String, default: 'TND' },
  frequency: { type: String, enum: ['ONE_TIME', 'MONTHLY', 'YEARLY'], default: 'MONTHLY' },
  startDate: { type: Date, default: Date.now },
  endDate: { type: Date },
  status: { type: String, enum: ['ACTIVE', 'PAUSED', 'ENDED'], default: 'ACTIVE' },
}, { timestamps: true });

module.exports = mongoose.model('Sponsorship', schema);
