package com.hpy.ops360.AssetService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.AssetService.entity.Asset;


@Repository
public interface AssetRepository extends JpaRepository<Asset, String> {

	
	@Query(value = "SELECT a.asset_id FROM Asset a WHERE a.file_name = :filename", nativeQuery = true)
    String findAssetIdByFilename(String filename);
}
