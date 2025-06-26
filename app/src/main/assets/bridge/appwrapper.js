/**
 * AppWrapper.js
 * * This script uses the WebViewBridge library and integrates with the Gemini API.
 * It now includes a "mock mode" for easier testing in desktop browsers
 * and provides clear on-screen diagnostics.
 */

document.addEventListener('DOMContentLoaded', function() {

    // --- 1. Get references to DOM elements ---
    const logArea = document.getElementById('response-area');
    const loader = document.getElementById('ai-loader');
    const statusBox = document.getElementById('status-box');

    // Standard buttons
    const btnShowToast = document.getElementById('btnShowToast');
    const btnSendUser = document.getElementById('btnSendUser');
    const btnGetInfo = document.getElementById('btnGetInfo');

    // Gemini buttons
    const btnGenerateWelcome = document.getElementById('btnGenerateWelcome');
    const btnAnalyzeUser = document.getElementById('btnAnalyzeUser');

    // --- 2. Check for Bridge, Initialize, and Set Mode ---
    let isNativeMode = false;

    if (typeof WebViewBridge !== 'undefined' && window.AndroidBridge && typeof window.AndroidBridge.postMessage === 'function') {
        WebViewBridge.init('AndroidBridge');
        isNativeMode = true;
        statusBox.textContent = 'Status: Native Mode Active ✅';
        statusBox.style.color = '#28a745'; // Green
        console.log('Bridge initialized in Native Mode.');
    } else {
        statusBox.textContent = 'Status: Browser Mode Active ⚠️ (Native features are mocked)';
        statusBox.style.color = '#ffc107'; // Yellow
        console.warn('WebViewBridge or AndroidBridge not found. Running in Browser (Mock) Mode.');
        if (typeof WebViewBridge === 'undefined') {
            document.body.innerHTML = '<h1 style="color: red; text-align: center; padding-top: 50px;">FATAL ERROR: WebViewBridge.js did not load.</h1>';
            return;
        }
    }


    // --- 3. Gemini API Integration ---

    function setAILoading(isLoading) {
        loader.style.display = isLoading ? 'block' : 'none';
    }

    async function callGeminiAPI(prompt) {
        setAILoading(true);
        logArea.textContent = '✨ Thinking...';
        
        const apiKey = ""; // Left empty as per instructions for Canvas environment
        const apiUrl = `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=${apiKey}`;

        const payload = {
            contents: [{
                role: "user",
                parts: [{ text: prompt }]
            }]
        };

        try {
            const response = await fetch(apiUrl, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
            if (!response.ok) throw new Error(`API Error: ${response.status}`);
            const result = await response.json();
            if (result.candidates && result.candidates[0]?.content?.parts[0]?.text) {
                return result.candidates[0].content.parts[0].text;
            } else {
                throw new Error('Could not extract text from Gemini API response.');
            }
        } catch (error) {
            console.error('Error calling Gemini API:', error);
            return `An error occurred: ${error.message}`;
        } finally {
            setAILoading(false);
        }
    }

    // --- 4. Define handler functions for UI interactions ---
    
    function showToast() {
        if (isNativeMode) {
            WebViewBridge.send({ action: 'showToast', data: { message: 'Hello from your Web App!', duration: 'long' } });
        } else {
            logArea.textContent = '[Mock Mode] "Show Native Toast" button clicked. A real toast would appear on a device.';
        }
    }

    function sendUserData() {
        const userData = { username: 'WebAppUser', level: 15, lastLogin: new Date().toISOString() };
        if (isNativeMode) {
            WebViewBridge.send({ action: 'saveUser', data: userData });
        }
        logArea.textContent = `[${isNativeMode ? 'Native' : 'Mock'} Mode] Sent user data. Try analyzing it next!`;
    }

    function getDeviceInfo(callback) {
        logArea.textContent = `Requesting device info...`;
        if (isNativeMode) {
            WebViewBridge.send({ action: 'getDeviceInfo' }, callback);
        } else {
            // In browser mode, return mock data after a short delay to simulate an async call
            const mockDeviceInfo = {
                os: 'MockOS (Browser)',
                version: 'N/A',
                manufacturer: 'Your Computer',
                model: 'Web Browser'
            };
            setTimeout(() => {
                logArea.textContent = 'Returning MOCK device info for browser testing.';
                callback(mockDeviceInfo);
            }, 500);
        }
    }

    // Gemini-powered handlers
    function generateWelcomeMessage() {
        logArea.textContent = 'First, getting device info...';
        getDeviceInfo(async function(deviceInfo) {
            const prompt = `Generate a short, friendly and slightly techy welcome message for a user. The app is running on a ${deviceInfo.manufacturer} ${deviceInfo.model}. Make it sound cool and personal.`;
            const welcomeMessage = await callGeminiAPI(prompt);
            logArea.textContent = welcomeMessage;
        });
    }

    async function analyzeUserData() {
        const userData = {
            username: 'CyberKnight77',
            level: 24,
            class: 'Chronomancer',
            achievements: ['First Quest', 'Master of Time', 'Guild Leader'],
            lastLogin: '2024-05-20T10:00:00Z',
            playtimeHours: 152
        };
        const prompt = `Analyze the following JSON data for a game user. Based on their data, write a fun, epic-sounding, one-paragraph status update or a short "prophecy" about their future in the game. User data: ${JSON.stringify(userData)}`;
        const analysis = await callGeminiAPI(prompt);
        logArea.textContent = analysis;
    }

    // --- 5. Attach event listeners to all buttons ---
    if(btnShowToast) btnShowToast.addEventListener('click', showToast);
    if(btnSendUser) btnSendUser.addEventListener('click', () => sendUserData()); // Wrapped in arrow to avoid issues
    if(btnGetInfo) btnGetInfo.addEventListener('click', () => getDeviceInfo(deviceInfo => {
        logArea.textContent = 'Device Info Received:\n\n' + JSON.stringify(deviceInfo, null, 2);
    }));
    if(btnGenerateWelcome) btnGenerateWelcome.addEventListener('click', generateWelcomeMessage);
    if(btnAnalyzeUser) btnAnalyzeUser.addEventListener('click', analyzeUserData);
    
    console.log('AppWrapper.js initialized and event listeners are active.');
});