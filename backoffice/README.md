# Backoffice - Visa Management System

This README explains the overall logic, architecture, and core workflows of the backoffice module in the Visa Management System.

## 🏗 System Architecture & Technologies

The backoffice is built as a **Spring Boot** web application leveraging both Server-Side Rendering (SSR) and Client-Side REST API calls:
- **Backend Framework:** Spring Boot 3 (Java)
- **Database Access:** Spring Data JPA / Hibernate
- **Templating Engine:** Thymeleaf
- **Frontend Interactivity:** Vanilla JavaScript (`callApi.js` custom wrapper for `fetch` API)
- **Data Transfer Objects (DTO):** Pattern used to maintain separation between exact database representations (Models) and what is exposed/consumed via REST endpoints.

Rather than Thymeleaf handling all form submissions directly via typical `POST` actions, the pages rely heavily on asynchronous JavaScript. Thymeleaf renders the page structure, and the frontend JavaScript calls endpoints exposed by `@RestController` controllers (e.g., `DemandeVisaRestController`) to load lookup data or submit form payloads.

## 🧠 Core Business Logic Flow

### 1. Page Display (Thymeleaf)
The `HomeController` handles standard browser navigation (e.g., `/visa-saisie`, `/visa-list`, `/duplicata-saisie`). It resolves to the appropriate HTML templates located in `resources/templates/`.

### 2. Form Setup & Lookup Data
When a user accesses a creation form (e.g., Visa Request), the page uses JS to query the backend for lookup data (`/demande-visa/demande-saisie-form`).
The `DemandeVisaService` retrieves all metadata required for the form selection options:
- `Genre`
- `SituationFamiliale`
- `Nationalite`
- `VisaType`
- `PieceJustificative`

### 3. Application Submission (Demande de Visa / Transfert / Duplicata)
Forms are submitted via `POST` requests handling JSON structures mapped to specific DTOs (e.g., `DemandeVisaSaisieDTO` or `TransfertVisaSaisieDTO`).

**The submission logic in `DemandeVisaService` primarily performs:**
- Entity mapping from the incoming DTO.
- Passport relationship creation (checking validity, ensuring constraints like maximum passport duration and issue dates).
- Instantiating appropriate entity records (`Demande`, `Demandeur`, `Passeport`, etc.).
- Persisting linked documents (`DemandePieceJustificative`).
- Assigning proper request statuses (`StatutDemande`) and keeping track via the history table (`HistoriqueStatutDemande`).

### 4. Special Operations
- **Transfert Visa:** Evaluates an established visa and handles linking the old records to the new request context.
- **Reference Lookups:** Dedicated lookups for existing Resident Cards (`CarteResidentRestController` / `searchCarteResidentByReference`) and past Visas (`VisaRestController` / `searchVisaByReference`) are provided.

## � Workflows & Business Logic (Métiers)

The application handles three primary workflows, and several cross-cutting business rules (métiers).

### 1. Demande de Visa (New Visa Request)
- **Workflow:** User accesses `/visa-saisie`, fills out applicant identity, passport data, requests a specific visa type, and attaches justification documents.
- **Business Rules:**
  - Mandatory fields are verified strictly.
  - The passport date of issue must fall within an acceptable range (e.g., maximum 12 months beforehand, etc.) and it cannot be expired.
  - A new identity (`Demandeur`) is created or updated, paired with their `Passeport`.
  - Initial `StatutDemande` (status) is attributed, and logged inside the `HistoriqueStatutDemande`.

### 2. Demande de Duplicata (Visa Reprint Request)
- **Workflow:** In case of loss or theft, an applicant can request a reprint of an existing valid visa at `/duplicata-saisie`.
- **Business Rules:**
  - The applicant must provide their original Visa reference to look it up.
  - The API verifies that the associated visa exists, is valid, and actually belongs to the provided identity.
  - Generates a new `Demande` typed as a duplicata, linking to the `Visa` context.

### 3. Demande de Transfert (Visa Transfer Request)
- **Workflow:** If someone receives a new passport but still has a valid visa on their old one, they request a transfer at `/transfert-visa-saisie`.
- **Business Rules:**
  - Verification of the existing visa against the old passport.
  - Validation of the newly assigned passport data.
  - The previous visa serves as the baseline, and a new request is formed, creating the bridge to print the valid visa duration on the new passport.

### 4. Backoffice Validation
- **Workflow:** Administrators can view the list of pending application requests through `/visa-list`.
- **Business Rules:**
  - Updates to statuses trigger new instances in `HistoriqueStatutDemande`. Statuses generally progress backward or forward through standard milestones (e.g., *Nouveau* ➔ *En cours d'examen* ➔ *Validé* / *Refusé*).

## �📂 Project Structure Overview

### Controllers (`com.itu.visabackoffice.controller`)
- **`HomeController`**: Handles standard `.html` view routing.
- **`DemandeVisaRestController`**: Core API endpoints for interacting with Visa applications (create, update, list, fetching references).
- **`CarteResidentRestController`**: API endpoints specific to Resident Card logic.
- **`VisaRestController`**: API endpoints for querying and manipulating approved Visas.

### Services (`com.itu.visabackoffice.services`)
- **`DemandeVisaService`**: Encapsulates the entire domain logic for the app. Validates constraints, saves cascading structures (applicants, passports, applications, attachments), constructs unified API responses, and parses search data.
  - `getDonneesDemandeVisa()`: Retrieves lookup data required for a visa application form (genres, marital status, visa types, justificatory documents).
  - `enregistrerDemandeDuplicata(...)`: Handles the dual-save logic for a duplicate request, storing both a mock "completed" original demand and the new "pending" duplicate.
  - `enregistrerDemandeTransfertVisa(...)`: Handles the internal structural generation for transferring a visa, creating a legacy "completed" record and the current "pending" transfer.
  - `enregistrerDemandeVisa(...)`: Persists a standard new visa application along with associated `Demandeur`, `Passeport`, and `PieceJustificative` records.
  - `getListeDemandesComprises()`: Retrieves a unified list of all requests mapped into the comprehensive `DemandeVisaCplDTO` representation for the front-end list view.
  - `convertDemandeToDTO(...)`: Utility method converting the `Demande` JPA entity into the flattened `DemandeVisaCplDTO` structure.
  - `updateDemandeVisa(...)`: Updates an existing application payload. This operation is allowed only if its current status is "Pending" (En attente).
  - `searchCarteResidentByReference(...)`: Looks up resident card records based on a provided reference string.
  - `searchVisaByReference(...)`: Looks up an existing approved visa record via reference, which is heavily used during the transfer workflow.
  - `enregistrerTransfertVisa(...)`: Receives the DTO for a transfer request, processes the new passport details, and orchestrates the complete saving operation.

### Models & DTOs
- **Models (`com.itu.visabackoffice.models`)**: JPA Entities defining the schema (e.g., `Demande`, `Demandeur`, `Passeport`, `PieceJustificative`). Preferences apply Lombok annotations (`@Data`, `@NoArgsConstructor`) to eliminate boilerplate boilerplate code.
- **DTOs (`com.itu.visabackoffice.dto`)**: Include request payloads (`DemandeVisaSaisieDTO`), augmented structures for the frontend (`DonneesDemandeVisaDTO`), and standard API envelopes (`ApiResponse`).

### Frontend Assets (`src/main/resources/...`)
- **`templates/`**: Thymeleaf HTML partials and views.
- **`static/js/callApi.js`**: Core asynchronous HTTP interaction library shared across the diverse pages to maintain consistency and ease of REST response error handling.
