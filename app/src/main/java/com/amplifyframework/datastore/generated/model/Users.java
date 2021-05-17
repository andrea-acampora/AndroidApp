package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Users type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Users", authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class Users implements Model {
  public static final QueryField ID = field("Users", "id");
  public static final QueryField NOME = field("Users", "Nome");
  public static final QueryField COGNOME = field("Users", "Cognome");
  public static final QueryField EMAIL = field("Users", "Email");
  public static final QueryField DATA_NASCITA = field("Users", "DataNascita");
  public static final QueryField SESSO = field("Users", "Sesso");
  public static final QueryField PREFERENZA = field("Users", "Preferenza");
  public static final QueryField DESCRIZIONE = field("Users", "Descrizione");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String") String Nome;
  private final @ModelField(targetType="String") String Cognome;
  private final @ModelField(targetType="String") String Email;
  private final @ModelField(targetType="AWSDate") Temporal.Date DataNascita;
  private final @ModelField(targetType="Sesso") Sesso Sesso;
  private final @ModelField(targetType="Preferenza") Preferenza Preferenza;
  private final @ModelField(targetType="String") String Descrizione;
  public String getId() {
      return id;
  }
  
  public String getNome() {
      return Nome;
  }
  
  public String getCognome() {
      return Cognome;
  }
  
  public String getEmail() {
      return Email;
  }
  
  public Temporal.Date getDataNascita() {
      return DataNascita;
  }
  
  public Sesso getSesso() {
      return Sesso;
  }
  
  public Preferenza getPreferenza() {
      return Preferenza;
  }
  
  public String getDescrizione() {
      return Descrizione;
  }
  
  private Users(String id, String Nome, String Cognome, String Email, Temporal.Date DataNascita, Sesso Sesso, Preferenza Preferenza, String Descrizione) {
    this.id = id;
    this.Nome = Nome;
    this.Cognome = Cognome;
    this.Email = Email;
    this.DataNascita = DataNascita;
    this.Sesso = Sesso;
    this.Preferenza = Preferenza;
    this.Descrizione = Descrizione;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Users users = (Users) obj;
      return ObjectsCompat.equals(getId(), users.getId()) &&
              ObjectsCompat.equals(getNome(), users.getNome()) &&
              ObjectsCompat.equals(getCognome(), users.getCognome()) &&
              ObjectsCompat.equals(getEmail(), users.getEmail()) &&
              ObjectsCompat.equals(getDataNascita(), users.getDataNascita()) &&
              ObjectsCompat.equals(getSesso(), users.getSesso()) &&
              ObjectsCompat.equals(getPreferenza(), users.getPreferenza()) &&
              ObjectsCompat.equals(getDescrizione(), users.getDescrizione());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getNome())
      .append(getCognome())
      .append(getEmail())
      .append(getDataNascita())
      .append(getSesso())
      .append(getPreferenza())
      .append(getDescrizione())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Users {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("Nome=" + String.valueOf(getNome()) + ", ")
      .append("Cognome=" + String.valueOf(getCognome()) + ", ")
      .append("Email=" + String.valueOf(getEmail()) + ", ")
      .append("DataNascita=" + String.valueOf(getDataNascita()) + ", ")
      .append("Sesso=" + String.valueOf(getSesso()) + ", ")
      .append("Preferenza=" + String.valueOf(getPreferenza()) + ", ")
      .append("Descrizione=" + String.valueOf(getDescrizione()))
      .append("}")
      .toString();
  }
  
  public static BuildStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   * @throws IllegalArgumentException Checks that ID is in the proper format
   */
  public static Users justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new Users(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      Nome,
      Cognome,
      Email,
      DataNascita,
      Sesso,
      Preferenza,
      Descrizione);
  }
  public interface BuildStep {
    Users build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep nome(String nome);
    BuildStep cognome(String cognome);
    BuildStep email(String email);
    BuildStep dataNascita(Temporal.Date dataNascita);
    BuildStep sesso(Sesso sesso);
    BuildStep preferenza(Preferenza preferenza);
    BuildStep descrizione(String descrizione);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private String Nome;
    private String Cognome;
    private String Email;
    private Temporal.Date DataNascita;
    private Sesso Sesso;
    private Preferenza Preferenza;
    private String Descrizione;
    @Override
     public Users build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Users(
          id,
          Nome,
          Cognome,
          Email,
          DataNascita,
          Sesso,
          Preferenza,
          Descrizione);
    }
    
    @Override
     public BuildStep nome(String nome) {
        this.Nome = nome;
        return this;
    }
    
    @Override
     public BuildStep cognome(String cognome) {
        this.Cognome = cognome;
        return this;
    }
    
    @Override
     public BuildStep email(String email) {
        this.Email = email;
        return this;
    }
    
    @Override
     public BuildStep dataNascita(Temporal.Date dataNascita) {
        this.DataNascita = dataNascita;
        return this;
    }
    
    @Override
     public BuildStep sesso(Sesso sesso) {
        this.Sesso = sesso;
        return this;
    }
    
    @Override
     public BuildStep preferenza(Preferenza preferenza) {
        this.Preferenza = preferenza;
        return this;
    }
    
    @Override
     public BuildStep descrizione(String descrizione) {
        this.Descrizione = descrizione;
        return this;
    }
    
    /** 
     * WARNING: Do not set ID when creating a new object. Leave this blank and one will be auto generated for you.
     * This should only be set when referring to an already existing object.
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     * @throws IllegalArgumentException Checks that ID is in the proper format
     */
    public BuildStep id(String id) throws IllegalArgumentException {
        this.id = id;
        
        try {
            UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
        } catch (Exception exception) {
          throw new IllegalArgumentException("Model IDs must be unique in the format of UUID.",
                    exception);
        }
        
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String nome, String cognome, String email, Temporal.Date dataNascita, Sesso sesso, Preferenza preferenza, String descrizione) {
      super.id(id);
      super.nome(nome)
        .cognome(cognome)
        .email(email)
        .dataNascita(dataNascita)
        .sesso(sesso)
        .preferenza(preferenza)
        .descrizione(descrizione);
    }
    
    @Override
     public CopyOfBuilder nome(String nome) {
      return (CopyOfBuilder) super.nome(nome);
    }
    
    @Override
     public CopyOfBuilder cognome(String cognome) {
      return (CopyOfBuilder) super.cognome(cognome);
    }
    
    @Override
     public CopyOfBuilder email(String email) {
      return (CopyOfBuilder) super.email(email);
    }
    
    @Override
     public CopyOfBuilder dataNascita(Temporal.Date dataNascita) {
      return (CopyOfBuilder) super.dataNascita(dataNascita);
    }
    
    @Override
     public CopyOfBuilder sesso(Sesso sesso) {
      return (CopyOfBuilder) super.sesso(sesso);
    }
    
    @Override
     public CopyOfBuilder preferenza(Preferenza preferenza) {
      return (CopyOfBuilder) super.preferenza(preferenza);
    }
    
    @Override
     public CopyOfBuilder descrizione(String descrizione) {
      return (CopyOfBuilder) super.descrizione(descrizione);
    }
  }
  
}
