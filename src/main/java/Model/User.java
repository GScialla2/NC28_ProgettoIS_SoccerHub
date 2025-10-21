package Model;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

public class User
{
    private int id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String salt;
    private Date birthDate;
    private String nationality;

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getSurname()
    {
        return surname;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }
    
    public String getSalt()
    {
        return salt;
    }

    public Date getBirthDate()
    {
        return birthDate;
    }

    public String getNationality()
    {
        return nationality;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setPassword(String password)
    {
        try
        {
            // Generate a random salt
            SecureRandom random = new SecureRandom();
            byte[] saltBytes = new byte[16];
            random.nextBytes(saltBytes);
            this.salt = Base64.getEncoder().encodeToString(saltBytes);
            
            // Use SHA-256 with salt for password hashing
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(saltBytes);
            digest.update(password.getBytes(StandardCharsets.UTF_8));
            this.password = Base64.getEncoder().encodeToString(digest.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Verifies if the provided password matches the stored password
     * @param inputPassword The password to verify
     * @return true if the password matches, false otherwise
     */
    public boolean verifyPassword(String inputPassword)
    {
        try
        {
            // Decode the stored salt
            byte[] saltBytes = Base64.getDecoder().decode(this.salt);
            
            // Hash the input password with the same salt
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(saltBytes);
            digest.update(inputPassword.getBytes(StandardCharsets.UTF_8));
            String hashedInput = Base64.getEncoder().encodeToString(digest.digest());
            
            // Compare the hashed input with the stored password
            return this.password.equals(hashedInput);
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }

    public void setNationality(String nationality)
    {
        this.nationality = nationality;
    }
    
    /**
     * Sets the salt for password hashing
     * @param salt The salt value
     */
    public void setSalt(String salt)
    {
        this.salt = salt;
    }
}

