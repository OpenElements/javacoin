// SPDX-License-Identifier: Apache-2.0
pragma solidity ^0.8.0;

import "./contracts/token/ERC20/ERC20.sol";
import "./contracts/utils/Strings.sol";

//import "https://github.com/OpenZeppelin/openzeppelin-contracts/blob/master/contracts/token/ERC20/ERC20.sol";

contract JavaCoinContract is ERC20 {

    address payable _owner;
    address payable _treasury;
    uint256 _coinScale;
    uint256 constant _hBarScale = 10 ** 8;

    uint256 _price;

    constructor(uint256 initialSupply) ERC20("JavaLand Coin", "JC") {
        _owner = payable(msg.sender);
        _treasury = payable(address(this));
        _coinScale = 10 ** decimals();
        _mint(address(this), initialSupply);
        _recalculatePrice(0);
    }

    function _recalculatePrice(int delta) private {
        uint256 halfSupply = totalSupply() / 2;
        uint256 balance = uint(int(balanceOf(_treasury)) + delta);

        if (balance > halfSupply) {
            uint256 deduct = 2 * (_coinScale * balance / halfSupply - _coinScale);
            _price = 10 * _hBarScale - _hBarScale * deduct * deduct * deduct / _coinScale / _coinScale / _coinScale;
        } else {
            uint256 deduct = 2 * (_coinScale - _coinScale * balance / halfSupply);
            _price = 10 * _hBarScale + _hBarScale * deduct * deduct * deduct / _coinScale / _coinScale / _coinScale;
        }
    }

    function getCoinPrice() public view returns (uint) {
        return _price;
    }

    function getCoinsForAccount() public view returns (uint) {
        return balanceOf(msg.sender);
    }

    function buyCoins(uint count) public payable {
        // check input parameters (count > 0 and count < 5% of total supply)
        // TODO

        uint total = count * _price / _coinScale;

        // check that user has enough HBars
        // TODO

        // check that pool contains enough JavaLandCoins
        // TODO

        // recalculate price
        _recalculatePrice(-int(count));

        // transfer HBars to contract
        // TODO

        // transfer JavaLandCoins to user
        // TODO
    }

    function sellCoins(uint count) public {
        // check input parameters (count > 0 and count < 10% of total supply)
        require(count > 0, "Count must be greater than 0");
        require(count <= totalSupply() / 10, "Not possible to buy more than 5% of total supply");
        uint total = count * _price / _coinScale;

        // check that user has enough JavaLandCoins
        require(balanceOf(msg.sender) >= count, "Not enough JavaLandCoins");

        // check that pool contains enough HBars
        require(_treasury.balance >= total, "Not enough HBars in pool");

        // recalculate price
        _recalculatePrice(int(count));

        // transfer JavaLandCoins to contract
        _transfer(msg.sender, _treasury, count);

        // transfer HBars to user
        (bool success,) = msg.sender.call{value : total}("");
        require(success, "Transfer failed.");
    }

    function withdrawHBars(uint amount) public {
        require(msg.sender == _owner);
        require(amount > 0);
        require(amount <= _treasury.balance);

        (bool success,) = _owner.call{value : amount}("");
        require(success, "Transfer failed.");
    }

    function mintCoins(uint amount) public {
        require(msg.sender == _owner);
        require(amount > 0);

        // add additional coins to the pool
        _mint(_treasury, amount);

        // recalculate price
        _recalculatePrice(0);
    }

    function burnCoins(uint amount) public {
        require(msg.sender == _owner);
        require(amount > 0);

        // check that pool contains enough JavaLandCoins
        require(balanceOf(_treasury) >= amount);

        // remove coins from the pool
        _burn(_treasury, amount);

        // recalculate price
        _recalculatePrice(0);
    }

    event Received(address, uint);

    receive() external payable {
        emit Received(msg.sender, msg.value);
    }

    fallback() external payable {}
}