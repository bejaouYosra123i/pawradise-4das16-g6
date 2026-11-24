const mongoose = require('mongoose');

const schema = new mongoose.Schema({
  title: { type: String, required: true, trim: true },
  description: { type: String, trim: true },
  goalAmount: { type: Number, required: true, min: 1 },
  currentAmount: { type: Number, default: 0, min: 0 },
  currency: { type: String, default: 'TND' },
  startDate: { type: Date, required: true },
  endDate: {
    type: Date,
    required: true,
    validate: {
      validator: function (v) { return !this.startDate || v >= this.startDate; },
      message: 'endDate doit être postérieure à startDate',
    },
  },
  status: { type: String, enum: ['DRAFT', 'ACTIVE', 'COMPLETED', 'CANCELLED'], default: 'DRAFT' },
  shelterId: { type: String, required: true }, // référence externe (Auth/profils)
  createdBy: { type: String },                 // userId
}, { timestamps: true });

module.exports = mongoose.model('FundraisingCampaign', schema);
