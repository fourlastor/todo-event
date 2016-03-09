// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: update_todo.proto at 3:1
package com.example.daniele.proto.todo;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class UpdateTodo extends Message<UpdateTodo, UpdateTodo.Builder> {
  public static final ProtoAdapter<UpdateTodo> ADAPTER = new ProtoAdapter_UpdateTodo();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_ID = "";

  public static final String DEFAULT_NAME = "";

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING",
      label = WireField.Label.REQUIRED
  )
  public final String id;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING",
      label = WireField.Label.REQUIRED
  )
  public final String name;

  public UpdateTodo(String id, String name) {
    this(id, name, ByteString.EMPTY);
  }

  public UpdateTodo(String id, String name, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.id = id;
    this.name = name;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.id = id;
    builder.name = name;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof UpdateTodo)) return false;
    UpdateTodo o = (UpdateTodo) other;
    return Internal.equals(unknownFields(), o.unknownFields())
        && Internal.equals(id, o.id)
        && Internal.equals(name, o.name);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (id != null ? id.hashCode() : 0);
      result = result * 37 + (name != null ? name.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (id != null) builder.append(", id=").append(id);
    if (name != null) builder.append(", name=").append(name);
    return builder.replace(0, 2, "UpdateTodo{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<UpdateTodo, Builder> {
    public String id;

    public String name;

    public Builder() {
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    @Override
    public UpdateTodo build() {
      if (id == null
          || name == null) {
        throw Internal.missingRequiredFields(id, "id",
            name, "name");
      }
      return new UpdateTodo(id, name, buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_UpdateTodo extends ProtoAdapter<UpdateTodo> {
    ProtoAdapter_UpdateTodo() {
      super(FieldEncoding.LENGTH_DELIMITED, UpdateTodo.class);
    }

    @Override
    public int encodedSize(UpdateTodo value) {
      return ProtoAdapter.STRING.encodedSizeWithTag(1, value.id)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.name)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, UpdateTodo value) throws IOException {
      ProtoAdapter.STRING.encodeWithTag(writer, 1, value.id);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.name);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public UpdateTodo decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.id(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.name(ProtoAdapter.STRING.decode(reader)); break;
          default: {
            FieldEncoding fieldEncoding = reader.peekFieldEncoding();
            Object value = fieldEncoding.rawProtoAdapter().decode(reader);
            builder.addUnknownField(tag, fieldEncoding, value);
          }
        }
      }
      reader.endMessage(token);
      return builder.build();
    }

    @Override
    public UpdateTodo redact(UpdateTodo value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}