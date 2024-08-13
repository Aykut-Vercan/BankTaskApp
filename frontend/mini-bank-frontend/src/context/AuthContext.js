import React, { createContext, useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);
  const navigate = useNavigate();

  // parametre ile login
  const login = async (username, password) => {
    try {
      const response = await axios.post('http://localhost:9191/api/users/login', null, {
        params: {
          username,
          password
        }
      });
      setToken(response.data);
      setUser(username);
      localStorage.setItem('token', response.data);
      navigate('/account');
    } catch (error) {
      console.error('Login failed:', error);
      alert('Login failed, please check your credentials.');
      navigate('/login');
    }
  };

  //  kaydetme fonksioynu
  const register = async (username, email, password) => {
    try {
      await axios.post('http://localhost:9191/api/users/register', null, {
        params: {
          username,
          password,
          email
        }
      });
      alert('user register successfully, please log in.');
      navigate('/login');
    } catch (error) {
      console.error('Registration failed:', error);
      alert('Registration failed, please try again. ');
      navigate('/register');
    }
  };

  // localstroage da token kontrolu yapıyoruz ininital renderingde
  useEffect(() => {
    const storedToken = localStorage.getItem('token');
    if (storedToken) {
      setToken(storedToken);
      setUser('Username');
    }
  }, []);

  return (
    <AuthContext.Provider value={{ user, token, login, register }}>
      {children}
    </AuthContext.Provider>
  );
};
