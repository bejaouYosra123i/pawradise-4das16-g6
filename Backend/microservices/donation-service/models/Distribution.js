const mongoose = require('mongoose');

const schema = new mongoose.Schema({
  allocationId: { type: mongoose.Schema.Types.ObjectId, ref: 'ResourceAllocation', required: true },
  status: { type: String, enum: ['PREPARING', 'IN_TRANSIT', 'DELIVERED', 'CANCELLED'], default: 'PREPARING' },
  recipientId: { type: String },   // shelterId / équipe (externe)
  deliveredAt: { type: Date },
  trackingNote: { type: String, trim: true },
}, { timestamps: true });

module.exports = mongoose.model('Distribution', schema);
