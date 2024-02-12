// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract PasswordManager {
    // address public owner;

    struct Password {
        string appName;
        string emailOrMobile;
        string password;
    }

    mapping(address => Password[]) private userPasswords;

    event PasswordAdded(address indexed user, string appName, string emailOrMobile);

    // modifier onlyOwner() {
    //     require(msg.sender == owner, "Not the owner");
    //     _;
    // }

    // modifier onlyPasswordOwner(uint index) {
    //     require(msg.sender == owner || msg.sender == getPasswordOwner(index), "Not authorized");
    //     _;
    // }

    // constructor() {
    //     owner = msg.sender;
    // }

    function addPassword(string memory _appName, string memory _emailOrMobile, string memory _password) external {
        Password memory newPassword = Password(_appName, _emailOrMobile, _password);
        userPasswords[msg.sender].push(newPassword);
        emit PasswordAdded(msg.sender, _appName, _emailOrMobile);
    }

    // function getPassword(uint index) external view onlyPasswordOwner(index) returns (string memory, string memory) {
    //     Password storage password = userPasswords[msg.sender][index];
    //     return (password.appName, password.password);
    // }

    function getAllPasswords() external view returns (Password[] memory) {
        return userPasswords[msg.sender];
    }

    function getPasswordOwner(uint index) public view returns (address) {
        require(index < userPasswords[msg.sender].length, "Index out of bounds");
        return msg.sender;
    }

    // function changeOwner(address newOwner) external onlyOwner {
    //     owner = newOwner;
    // }
}
