package org.dyndns.jkiddo.logic.desk;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.dyndns.jkiddo.dmap.chunks.audio.AlbumArtist;
import org.dyndns.jkiddo.dmap.chunks.audio.SongAlbum;
import org.dyndns.jkiddo.dmap.chunks.audio.SongAlbumId;
import org.dyndns.jkiddo.dmap.chunks.audio.SongArtist;
import org.dyndns.jkiddo.dmap.chunks.audio.SongArtistId;
import org.dyndns.jkiddo.dmap.chunks.audio.SongArtworkCount;
import org.dyndns.jkiddo.dmap.chunks.audio.SongBitrate;
import org.dyndns.jkiddo.dmap.chunks.audio.SongCodecType;
import org.dyndns.jkiddo.dmap.chunks.audio.SongDataKind;
import org.dyndns.jkiddo.dmap.chunks.audio.SongDateAdded;
import org.dyndns.jkiddo.dmap.chunks.audio.SongDateModified;
import org.dyndns.jkiddo.dmap.chunks.audio.SongDescription;
import org.dyndns.jkiddo.dmap.chunks.audio.SongDiscNumber;
import org.dyndns.jkiddo.dmap.chunks.audio.SongExtraData;
import org.dyndns.jkiddo.dmap.chunks.audio.SongFormat;
import org.dyndns.jkiddo.dmap.chunks.audio.SongGenre;
import org.dyndns.jkiddo.dmap.chunks.audio.SongSampleRate;
import org.dyndns.jkiddo.dmap.chunks.audio.SongSize;
import org.dyndns.jkiddo.dmap.chunks.audio.SongTime;
import org.dyndns.jkiddo.dmap.chunks.audio.SongTrackNumber;
import org.dyndns.jkiddo.dmap.chunks.audio.SongYear;
import org.dyndns.jkiddo.dmap.chunks.audio.SortAlbum;
import org.dyndns.jkiddo.dmap.chunks.audio.SortAlbumArtist;
import org.dyndns.jkiddo.dmap.chunks.audio.SortArtist;
import org.dyndns.jkiddo.dmap.chunks.audio.SortName;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.ExtendedMediaKind;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.MediaKind;
import org.dyndns.jkiddo.dmp.chunks.media.ContainerItemId;
import org.dyndns.jkiddo.dmp.chunks.media.ItemId;
import org.dyndns.jkiddo.dmp.chunks.media.ItemKind;
import org.dyndns.jkiddo.dmp.chunks.media.ItemName;
import org.dyndns.jkiddo.dmp.chunks.media.Listing;
import org.dyndns.jkiddo.dmp.chunks.media.ListingItem;
import org.dyndns.jkiddo.dmp.chunks.media.PersistentId;
import org.dyndns.jkiddo.dmp.model.MediaItem;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.Track;
import com.github.felixgail.gplaymusic.model.enums.StreamQuality;
import com.github.felixgail.gplaymusic.util.TokenProvider;

import svarzee.gps.gpsoauth.AuthToken;
import svarzee.gps.gpsoauth.Gpsoauth.TokenRequestFailed;

public class GoogleStoreReader implements IMusicStoreReader
{
	
	private final GPlayMusic gm;
	private final Collection<Track> songs;

	public GoogleStoreReader(final String username, final String password, String androidId) throws IOException, URISyntaxException, TokenRequestFailed
	{
	    AuthToken authToken = TokenProvider.provideToken(username, password, androidId);
		gm = new GPlayMusic.Builder().setDebug(false).setAuthToken(authToken).setAndroidID(androidId).build();
		gm.getConfig().put("isNautilusUser", "true");
System.err.println(gm.getConfig());
System.err.println("GPM: start");
		songs = gm.getTrackApi().getLibraryTracks();
System.err.println("GPM: done: " + songs.size());
	}

    @Override
	public Collection<MediaItem> readTunes() throws Exception
	{
System.err.println("readTunes: " + songs.size());
		return songs.stream().map(song -> {
				final MediaItem mi = new MediaItem();
				mi.setSongArtist(song.getArtist());
				mi.setSongAlbum(song.getAlbum());
				mi.setItemName(song.getTitle());
				mi.setSongTime((int) (long) song.getDurationMillis());
				mi.setExternalIdentifer(song.getID());
				return mi;
		}).collect(Collectors.toList());
	}
	@Override
	public URI getTune(final String tune) throws Exception
	{
		final Track song = gm.getTrackApi().getTrack(tune);
		return song.getStreamURL(StreamQuality.HIGH).toURI();
	}

	@Override
	public void readTunesMemoryOptimized(final Listing listing, final Map<Long, String> map) throws Exception
	{
System.err.println("readTunesMemoryOptimized: " + songs.size());
		final AtomicLong id = new AtomicLong();

        for(final Track song : songs)
        {
            final ListingItem item = new ListingItem();
            item.add(new ItemKind(ItemKind.AUDIO));
            item.add(new SongDataKind(SongDataKind.UNKNOWN));
            item.add(new SongArtist(song.getArtist()));
            item.add(new SongArtistId(UUID.nameUUIDFromBytes(song.getArtist().getBytes()).getMostSignificantBits()));
            item.add(new SongSize(0x430000));
            item.add(new SongTime(song.getDurationMillis()));
            item.add(new SongDateAdded(System.currentTimeMillis() / 1000));
            item.add(new ItemName(song.getTitle()));
            item.add(new SongGenre(song.getGenre().get()));
            item.add(new ContainerItemId(id.get()));
            item.add(new ItemId(id.get()));
            item.add(new SongSampleRate(SongSampleRate.KHZ_44100));
            item.add(new SongDescription("MPEG audio file"));
            item.add(new SongAlbum(song.getAlbum()));
            item.add(new SongAlbumId(UUID.nameUUIDFromBytes(song.getAlbum().getBytes()).getMostSignificantBits()));
            item.add(new SongBitrate(0x80));
            item.add(new ExtendedMediaKind(ExtendedMediaKind.UNKNOWN_ONE));
            item.add(new AlbumArtist(song.getAlbumArtist()));
            item.add(new SortArtist(song.getArtist()));
            item.add(new SongTrackNumber(song.getTrackNumber()));
            item.add(new SongDiscNumber(song.getDiscNumber()));
            item.add(new SongYear(song.getYear().getAsInt()));

            item.add(new SongDateModified(System.currentTimeMillis() / 1000 / 3600 / 24));
            item.add(new SortName(song.getTitle()));
            item.add(new SongCodecType(SongCodecType.MPEG));
            item.add(new PersistentId(id.get()));
            item.add(new MediaKind(MediaKind.KIND_1));
            item.add(new SortAlbum(song.getAlbum()));
            item.add(new SongFormat(SongFormat.MP3));
            item.add(new SortAlbumArtist(song.getArtist()));
            item.add(new SongExtraData(1));
            item.add(new SongArtworkCount(0));

            listing.add(item);
            map.put(Long.valueOf(id.getAndIncrement()), song.getID());
//System.err.println(song.getID() + ": " + id);
        }
	}
}
