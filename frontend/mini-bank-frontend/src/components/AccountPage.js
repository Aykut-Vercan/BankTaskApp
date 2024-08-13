// src/pages/AccountPage.js
import React, { useState, useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import axios from 'axios';

const AccountPage = () => {
    const { token } = useContext(AuthContext);
    const [accountName, setAccountName] = useState('');
    const [accountNameForSearch, setAccountNameForSearch] = useState('');
    const [initialBalance, setInitialBalance] = useState('');
    const [accounts, setAccounts] = useState([]);

    const handleCreateAccount = async () => {//action type a gore işlem
        try {
            await axios.post('http://localhost:9191/api/accounts', {
                action: 'create',
                name: accountName,
                initialBalance: parseFloat(initialBalance),//doublealan
            }, {
                headers: { Authorization: `Bearer ${token}` }
            });
            alert('Account created successfully');
        } catch (error) {
            console.error('Error creating account:', error);
            alert('Failed to create account.');
        }
    };

    const handleSearchAccounts = async () => {
        try {
            const response = await axios.post('http://localhost:9191/api/accounts', {
                action: 'search',
                name: accountName,
            }, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setAccounts(response.data);
        } catch (error) {
            console.error('Error searching accounts:', error);
            alert('Failed to search accounts.');
        }
    };

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-2xl font-bold mb-4">Account Management</h1>
            <div className="space-y-4 mb-4">
                <h2 className="text-xl font-semibold">Create Account</h2>
                <input
                    type="text"
                    placeholder="Account Name"
                    value={accountName}
                    onChange={(e) => setAccountName(e.target.value)}
                    className="p-2 border border-gray-300 rounded"
                />
                <input
                    type="number"
                    placeholder="Initial Balance"
                    value={initialBalance}
                    onChange={(e) => setInitialBalance(e.target.value)}
                    className="p-2 border border-gray-300 rounded ml-3"
                />
                <button
                    onClick={handleCreateAccount}
                    className="bg-blue-500 text-white p-2 rounded ml-3"
                >
                    Create Account
                </button>
            </div>
            <div className="space-y-4">
                <h2 className="text-xl font-semibold">Search Accounts</h2>
                <input
                    type="text"
                    placeholder="Account Name"
                    value={accountNameForSearch}
                    onChange={(e) => setAccountNameForSearch(e.target.value)}
                    className="p-2 border border-gray-300 rounded"
                />
                <button
                    onClick={handleSearchAccounts}
                    className="bg-blue-500 text-white p-2 rounded ml-3"
                >
                    Search Accounts
                </button>
                <div>
                    {accounts.length > 0 ? (
                        <ul>
                            {accounts.map((account) => (
                                <li key={account.id}>{account.name} - {account.balance}</li>
                            ))}
                        </ul>
                    ) : (
                        <p>No accounts found.</p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default AccountPage;
