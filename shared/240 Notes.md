# Domain Driven Design Principles

## 1. Iterative by nature: 

    DDD is not a one-time activity but an ongoing process that evolves with the system.
    It requires continuous refinement and adaptation as the domain and requirements change.
    Design and then code.

## 2. Abstraction: 

    DDD emphasizes the use of abstractions to model complex domains.
    Abstractions help in simplifying the domain and making it more understandable.
    They allow developers to focus on high-level concepts rather than low-level details.

## 3. Naming: 

    DDD emphasizes the importance of using a common language that is shared by both developers and domain experts.
    This helps in reducing misunderstandings and ensures that everyone is on the same page.
    The use of a ubiquitous language is crucial for effective communication.

## 4. Single Responsibility Principle (SRP): 

    Each module or component should have a single responsibility and should not be overloaded with multiple responsibilities.
    This helps in maintaining the modularity and maintainability of the system.

# Computer Security 3/17/25

## Cryptographic Hash Functions

    A cryptographic hash function is a mathematical algorithm that takes an input (or 'message') and returns a fixed-size string of bytes. The output is typically a 'digest' that is unique to each unique input.

    Hash functions are commonly used in computer security for tasks such as digital signatures and password hashing. They are designed to be fast and efficient, while also being resistant to attacks such as collisions and pre-image attacks.

    Some common cryptographic hash functions include MD5, SHA-1, and SHA-256. These functions are widely used in various security applications, including SSL/TLS, PGP, and IPsec.

    Cryptographic hash functions are an essential tool in modern computer security, and they play a crucial role in protecting sensitive data and ensuring the integrity of digital communications.

## Encryption / Decryption

    Plaint text: The original message or data that is to be encrypted.
    Cipher text: The encrypted message or data that is produced by the encryption algorithm.
    Key: A secret value that is used by the encryption algorithm to encrypt and decrypt the message.
    Key Size: The length of the key, which determines the strength of the encryption algorithm.

### Encryption Algorithms:

    Symmetric Key Encryption: Uses a single key for both encryption and decryption. Examples include DES, AES, and Blowfish.
    Asymmetric Key Encryption: Uses a pair of keys (public and private) for encryption and decryption. Examples include RSA, DSA, and ECC.