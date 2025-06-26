/**
 * WebViewBridge.js
 * * A modular and extendable JavaScript library for facilitating communication
 * between an HTML5 web app/game and a native Android application via a WebView.
 * * @version 1.0.0
 * @author Gemini
 */
(function(window) {
    'use strict';

    // The main bridge object that will be exposed on the window.
    const WebViewBridge = {};

    // --- Private Properties ---

    // The name of the JavaScript interface object exposed by the Android app.
    let _nativeBridgeName = 'AndroidBridge';
    // A map to store event listeners for events dispatched from the native side.
    const _eventListeners = new Map();
    // A counter to generate unique IDs for callbacks.
    let _callbackIdCounter = 0;
    // A map to store pending callbacks that are waiting for a response from the native side.
    const _pendingCallbacks = new Map();


    // --- Public Methods ---

    /**
     * Initializes the bridge. This should be called once when the web app loads.
     * @param {string} [bridgeName='AndroidBridge'] - The name of the JavaScript interface
     * object provided by the Android app. This must match the name used in `webView.addJavascriptInterface`.
     */
    WebViewBridge.init = function(bridgeName = 'AndroidBridge') {
        _nativeBridgeName = bridgeName;
        console.log(`WebViewBridge initialized with native interface: "${_nativeBridgeName}"`);
    };

    /**
     * Sends a message to the native Android app.
     * @param {object} message - The message object containing action and data.
     * @param {string} message.action - The name of the action for the native app to perform.
     * @param {object} [message.data] - The payload to send with the action.
     * @param {function} [callback] - An optional callback function to handle a response from the native side.
     */
    WebViewBridge.send = function(message, callback) {
        if (!window[_nativeBridgeName] || typeof window[_nativeBridgeName].postMessage !== 'function') {
            console.error(`Native bridge object "${_nativeBridgeName}" or its "postMessage" method is not available.`);
            return;
        }

        const messageToSend = {
            action: message.action,
            data: message.data || {}
        };

        if (typeof callback === 'function') {
            const callbackId = `cb_${_callbackIdCounter++}`;
            _pendingCallbacks.set(callbackId, callback);
            messageToSend.callbackId = callbackId;
        }

        const jsonMessage = JSON.stringify(messageToSend);
        window[_nativeBridgeName].postMessage(jsonMessage);
    };

    /**
     * Registers an event listener for an event dispatched from the native side.
     * @param {string} eventName - The name of the event to listen for.
     * @param {function} listener - The function to call when the event is received.
     */
    WebViewBridge.on = function(eventName, listener) {
        if (!_eventListeners.has(eventName)) {
            _eventListeners.set(eventName, []);
        }
        _eventListeners.get(eventName).push(listener);
    };

    /**
     * Unregisters an event listener.
     * @param {string} eventName - The name of the event.
     * @param {function} listener - The listener function to remove.
     */
    WebViewBridge.off = function(eventName, listener) {
        if (!_eventListeners.has(eventName)) {
            return;
        }
        const listeners = _eventListeners.get(eventName);
        const index = listeners.indexOf(listener);
        if (index > -1) {
            listeners.splice(index, 1);
        }
    };


    // --- Internal Handlers (Called by Native Code) ---

    /**
     * Handles a response from the native side that corresponds to a specific callback.
     * This function is not meant to be called directly by web app code.
     * @param {string} callbackId - The ID of the callback to execute.
     * @param {object} data - The response data from the native side.
     * @private
     */
    WebViewBridge._handleNativeCallback = function(callbackId, data) {
        if (_pendingCallbacks.has(callbackId)) {
            const callback = _pendingCallbacks.get(callbackId);
            callback(data);
            _pendingCallbacks.delete(callbackId); // Clean up to prevent memory leaks
        } else {
            console.warn(`Received callback for unknown ID: ${callbackId}`);
        }
    };

    /**
     * Handles a generic event dispatched from the native side.
     * This function is not meant to be called directly by web app code.
     * @param {string} eventName - The name of the event.
     * @param {object} data - The event data from the native side.
     * @private
     */
    WebViewBridge._handleNativeEvent = function(eventName, data) {
        if (_eventListeners.has(eventName)) {
            const listeners = _eventListeners.get(eventName);
            listeners.forEach(listener => {
                try {
                    listener(data);
                } catch (e) {
                    console.error(`Error in event listener for "${eventName}":`, e);
                }
            });
        }
    };

    // Expose the bridge object to the global scope.
    window.WebViewBridge = WebViewBridge;

})(window);