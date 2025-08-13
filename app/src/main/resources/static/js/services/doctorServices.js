const API_URL = "http://localhost:8080/api/doctors";

/**
 * Obtener todos los doctores
 */
export async function getDoctors() {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error("Error al obtener doctores");
    return await response.json();
  } catch (error) {
    console.error("Error en getDoctors:", error);
    return [];
  }
}

/**
 * Eliminar un doctor por ID
 */
export async function deleteDoctor(doctorId, token) {
  try {
    const response = await fetch(`${API_URL}/${doctorId}`, {
      method: "DELETE",
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });

    if (response.ok) {
      alert("Doctor eliminado correctamente");
      return true;
    } else {
      const errorText = await response.text();
      alert(`Error al eliminar doctor: ${errorText}`);
      return false;
    }
  } catch (error) {
    console.error("Error en deleteDoctor:", error);
    return false;
  }
}

/**
 * Crear un nuevo doctor
 */
export async function createDoctor(doctorData, token) {
  try {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      },
      body: JSON.stringify(doctorData)
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText);
    }

    return await response.json();
  } catch (error) {
    console.error("Error en createDoctor:", error);
    throw error;
  }
}
