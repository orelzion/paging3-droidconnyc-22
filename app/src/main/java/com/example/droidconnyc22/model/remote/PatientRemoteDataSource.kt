package com.example.droidconnyc22.model.remote

import com.example.droidconnyc22.model.Patient
import com.example.droidconnyc22.model.PatientDataSource

class PatientRemoteDataSource : PatientDataSource {
    override suspend fun getPatientListBy(filterId: String): List<Patient> {
        return listOf(
            PatientRemote(
                patientId = "1",
                name = "Orel Zion",
                bookmarkCount = 12,
                isBookmarked = false,
                photoUrl = "https://www.brainline.org/sites/default/files/slides/mri.jpg"
            ),
            PatientRemote(
                patientId = "2",
                name = "Sara Zion",
                bookmarkCount = 543,
                isBookmarked = true,
                photoUrl = null
            ),
            PatientRemote(
                patientId = "3",
                name = "Lorem Ipsum",
                bookmarkCount = 423,
                isBookmarked = false,
                photoUrl = "https://oryon.co.uk/app/uploads/2019/08/Brain-MRI-e1565353833878.jpg"
            ),
            PatientRemote(
                patientId = "4",
                name = "Soro teet Hugo",
                bookmarkCount = 756,
                isBookmarked = false,
                photoUrl = "https://www.esa.int/var/esa/storage/images/esa_multimedia/images/2013/06/mri_brain_scan/12915330-1-eng-GB/MRI_brain_scan_pillars.jpg"
            ),
            PatientRemote(
                patientId = "5",
                name = "Ercha joit",
                bookmarkCount = 12,
                isBookmarked = false,
                photoUrl = "https://www.kenhub.com/thumbor/zoz_XVCq44UFroH2ds6eoOUvdtA=/fit-in/800x1600/filters:watermark(/images/logo_url.png,-10,-10,0):background_color(FFFFFF):format(jpeg)/images/library/13517/ff.jpg"
            )
        )
    }

    override suspend fun updatePatient(patient: Patient) {
        TODO("Not yet implemented")
    }
}