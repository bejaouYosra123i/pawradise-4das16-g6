const mongoose = require('mongoose');

const schema = new mongoose.Schema({
  donorId: { type: mongoose.Schema.Types.ObjectId, ref: 'Donor', required: true },
  campaignId: { type: mongoose.Schema.Types.ObjectId, ref: 'FundraisingCampaign', required: true },
  amount: { type: Number, required: true, min: 1 },
  currency: { type: String, default: 'TND' },
  frequency: { type: String, enum: ['ONE_TIME', 'RECURRING'], default: 'ONE_TIME' },
  recurrenceInterval: {
    type: String,
    enum: ['WEEKLY', 'MONTHLY', 'YEARLY'],
    required: function () { return this.frequency === 'RECURRING'; },
  },
  paymentMethod: { type: String, enum: ['CARD', 'BANK_TRANSFER', 'CASH'], default: 'CARD' },
  paymentStatus: { type: String, enum: ['PENDING', 'COMPLETED', 'FAILED'], default: 'PENDING' }, // paiement simulé
  message: { type: String, trim: true },
}, { timestamps: true });

module.exports = mongoose.model('Donation', schema);
