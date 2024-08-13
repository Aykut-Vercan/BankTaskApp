// src/App.js
import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import LoginPage from './components/LoginPage';
import RegisterPage from './components/RegisterPage';
import AccountPage from './components/AccountPage';

function App() {
  return (
    <AuthProvider>
      <Routes>
      <Route path="/" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/account" element={<AccountPage />} />
      </Routes>
    </AuthProvider>
  );
}

export default App;
