module.exports = (err, req, res, next) => {
  if (err.name === 'ValidationError') {
    return res.status(400).json({
      message: 'Validation échouée',
      errors: Object.values(err.errors).map((e) => ({ field: e.path, message: e.message })),
    });
  }
  if (err.name === 'CastError') {
    return res.status(400).json({ message: `Valeur invalide pour ${err.path}` });
  }
  if (err.code === 11000) {
    return res.status(409).json({ message: 'Doublon', fields: err.keyValue });
  }
  console.error(err);
  res.status(500).json({ message: 'Erreur interne du serveur' });
};
