// src/pages/RegisterPage.js
import React, { useState, useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const RegisterPage = () => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const { register } = useContext(AuthContext);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        await register(username, email, password);
        navigate('/login');
    };

    return (
        <div className="container mx-auto p-4 ">
            <h1 className="text-2xl font-bold mb-4">Register</h1>
            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label htmlFor="username" className="block text-sm font-medium">Username</label>
                    <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        className="mt-1 block  p-2 border border-gray-300 rounded w-1/2"
                        required
                    />
                </div>
                <div>
                    <label htmlFor="email" className="block text-sm font-medium">Email</label>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        className="mt-1 block  p-2 border border-gray-300 rounded w-1/2"
                        required
                    />
                </div>
                <div>
                    <label htmlFor="password" className="block text-sm font-medium">Password</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        className="w-1/2 mt-1 block  p-2 border border-gray-300 rounded"
                        required
                    />
                </div>
                <button
                    type="submit"
                    className="w-1/2 bg-green-600 text-white p-2 rounded hover:bg-gray-100 hover:text-green-600 hover:outline outline-offset-0-o"
                >
                    Register
                </button>
            </form>
        </div>
    );
};

export default RegisterPage;
