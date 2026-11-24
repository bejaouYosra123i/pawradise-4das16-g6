const mongoose = require('mongoose');

const schema = new mongoose.Schema({
  donationId: { type: mongoose.Schema.Types.ObjectId, ref: 'Donation', required: true, unique: true },
  receiptNumber: { type: String, required: true, unique: true },
  amount: { type: Number, required: true, min: 0 },
  currency: { type: String, default: 'TND' },
  issuedAt: { type: Date, default: Date.now },
  pdfUrl: { type: String },
}, { timestamps: true });

module.exports = mongoose.model('DonationReceipt', schema);
