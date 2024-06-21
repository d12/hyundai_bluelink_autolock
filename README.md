# Hyundai BlueLink Auto-lock

For some reason, some Hyundai cars don't have proximity auto-lock when you leave your car. If you're used to this feature from a previous vehicle like me, you probably keep accidentally leaving your car unlocked in public 😬

This app solves the issue by locking the doors when you leave the car.

## Requirements
- A BlueLink-supported Hyundai vehicle in one of the supported countries.
- An Android device that you connect to the vehicle via BlueTooth.

## Supported countries
- Canada

## How it works
If you've configured your phone to connect to your car via BlueTooth (e.g. if you use Android Auto), your phone will automatically connect to the car when you start the car. When you turn your car off, your phone will disconnect from the car.

This app listens for this "disconnection" and triggers a lock when it happens. You can configure how long the app waits before locking the car. We trigger a lock via the BlueLink API using your BlueLink credentials.

## Risks
You can lock your fob inside the vehicle with BlueLink. If you do this, the only way to unlock the vehicle is with BlueLink or another keyfob. If BlueLink goes down and you don't have a second fob, you're screwed. Using this app increases the risk of locking your fob inside the vehicle, so please be careful!

## Contribution
There are two main things that contributors can help with:
- Adding support for more countries. My vehicle is in Canada so I don't have access to any other countries' BlueLink APIs.
- Developing an iOS port.
